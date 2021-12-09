package com.klutz.registry.client.beat;

import com.klutz.registry.client.TransportProxy;
import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.core.lifecycle.Closeable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class BeatReactor implements Closeable {

    private final Logger logger = LoggerFactory.getLogger(BeatReactor.class);

    private final TransportProxy transportProxy;

    private ScheduledThreadPoolExecutor executor;

    public BeatReactor(TransportProxy transportProxy) {
        this.transportProxy = transportProxy;
        executor = new ScheduledThreadPoolExecutor(1,new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("beat.sender");
                return thread;
            }
        });
    }

    public BeatInfo buildBeatInfo(InstanceInfo instance) {
        BeatInfo beatInfo = new BeatInfo();
        beatInfo.setInstanceId(instance.getInstanceId());
        beatInfo.setServiceName(instance.getAppName());
        beatInfo.setInstanceInfo(instance);
        return beatInfo;
    }

    public void addBeatInfo(BeatInfo beatInfo) {
        executor.schedule(new BeatTask(beatInfo),beatInfo.getPeriod(), TimeUnit.MILLISECONDS);
    }

    class BeatTask implements Runnable{

        private final BeatInfo beatInfo;

        public BeatTask(BeatInfo beatInfo) {
            this.beatInfo = beatInfo;
        }

        @Override
        public void run() {
            try {
                boolean success = transportProxy.sendBeat(beatInfo);
                if( !success ){
                    //重新发起注册
                    transportProxy.registerService(beatInfo.getInstanceInfo());
                }
            }catch (Throwable ex){
                logger.error("send beat error ",ex);
            }
            executor.schedule(new BeatTask(beatInfo),beatInfo.getPeriod(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void shutdown() throws Exception {
        executor.shutdown();
    }
}
