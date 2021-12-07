package com.klutz.registry.core.lease;

import com.klutz.registry.core.entity.InstanceInfo;

/**
 * created on 2021/12/7
 * @author klutz
 */
public class Lease{

    //默认实例清空时间
    public static final int DEFAULT_DURATION_IN_SECS = 90;

    private InstanceInfo holder;

    //注册时间
    private volatile long registrationTimestamp;
    private volatile long lastUpdateTimestamp;

    public Lease(InstanceInfo r) {
        holder = r;
        registrationTimestamp = System.currentTimeMillis();
        lastUpdateTimestamp = registrationTimestamp;
    }

    public InstanceInfo getHolder() {
        return holder;
    }

    public long getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    public void renew() {
        lastUpdateTimestamp = System.currentTimeMillis();
    }

    public boolean isExpired() {
        if( holder == null ){
            return true;
        }
        long durationMillis =  DEFAULT_DURATION_IN_SECS * 1000L;
        if( holder.getExpireSeconds()!=null){
            durationMillis = DEFAULT_DURATION_IN_SECS * 1000L;
        }
        return System.currentTimeMillis() > lastUpdateTimestamp + durationMillis;
    }
}
