package com.klutz.registry.client;

import com.klutz.registry.client.beat.BeatInfo;
import com.klutz.registry.client.beat.BeatReactor;
import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.core.exception.KlutzException;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class RegistryClient {

    private String serverList;

    private TransportProxy transportProxy;

    private BeatReactor beatReactor;

    public RegistryClient(Properties properties) throws KlutzException {
        init(properties);
    }

    private void init(Properties properties) throws KlutzException {
        serverList = properties.getProperty(PropertyKeyConst.SERVER_ADDR);

        transportProxy = new TransportProxy(serverList);
        beatReactor = new BeatReactor(transportProxy);
    }

    public void registerInstance( InstanceInfo instance) throws KlutzException {

        BeatInfo beatInfo = beatReactor.buildBeatInfo(instance);
        beatInfo.setPeriod(5000L);
        beatReactor.addBeatInfo(beatInfo);

        transportProxy.registerService(instance);
    }

}
