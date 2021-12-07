package com.klutz.registry.server.service;

import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.core.entity.ServiceInfo;
import com.klutz.registry.core.lease.Lease;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * created on 2021/12/7
 * @author klutz
 */
public abstract class AbstractPeerAwareInstanceRegistry implements PeerAwareInstanceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(AbstractPeerAwareInstanceRegistry.class);

    private final ConcurrentHashMap<String, Map<String, Lease>> registry
            = new ConcurrentHashMap<String, Map<String, Lease>>();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock read = readWriteLock.readLock();
    private final Lock write = readWriteLock.writeLock();

    private Timer evictionTimer = new Timer("Klutz-EvictionTimer", true);

    @PostConstruct
    public void postInit(){
        evictionTimer.schedule(new EvictTask(),5000,5000);
    }

    @Override
    public void register(InstanceInfo info, boolean isReplication) {
        try {
            read.lock();
            Map<String, Lease> service = registry.get(info.getAppName());
            if( service == null ){
                final ConcurrentHashMap<String, Lease> newService = new ConcurrentHashMap<String, Lease>();
                service = registry.put(info.getAppName(),newService);
                if( service == null){
                    service = newService;
                }
            }
            service.put(info.getInstanceId(),new Lease(info));
        }finally {
            read.unlock();
        }
    }

    @Override
    public boolean cancel(String appName, String instanceId, boolean isReplication) {
        try {
            read.lock();
            Map<String, Lease> service = registry.get(appName);
            if( service == null ){
                return false;
            }

            Lease lease = service.get(instanceId);
            if( lease == null){
                return false;
            }

            service.remove(instanceId,lease);

            return true;

        }finally {
            read.unlock();
        }
    }

    @Override
    public ServiceInfo getServiceInfo(String appName) {
        Map<String, Lease> leaseMap = registry.get(appName);
        if( leaseMap == null){
            return null;
        }
        ServiceInfo serviceInfo = null;
        for (Map.Entry<String, Lease> entry : leaseMap.entrySet()) {
            if (serviceInfo == null) {
                serviceInfo = new ServiceInfo(appName);
            }
            serviceInfo.addInstance(entry.getValue().getHolder());
        }
        return serviceInfo;
    }

    @Override
    public boolean renew(String appName, String instanceId, boolean isReplication) {
        Map<String, Lease> leaseMap = registry.get(appName);
        if( leaseMap == null){
            return false;
        }
        Lease lease = leaseMap.get(instanceId);
        if( lease == null){
            return false;
        }
        lease.renew();
        return true;
    }

    private void evict() {
        for (Map.Entry<String, Map<String, Lease>> groupEntry : registry.entrySet()) {
            Map<String, Lease> leaseMap = groupEntry.getValue();
            if (leaseMap != null) {
                Iterator<Map.Entry<String, Lease>> iterator = leaseMap.entrySet().iterator();
                while ( iterator.hasNext()){
                    Lease lease = iterator.next().getValue();
                    if( lease.isExpired() ){
                        iterator.remove();
                    }
                }
            }
        }
    }

    private class EvictTask extends TimerTask {
        @Override
        public void run() {
            try {
                evict();
            }catch (Throwable ex){
                logger.error("evict error ",ex);
            }
        }
    }
}
