package com.klutz.registry.client.beat;

import com.klutz.registry.core.entity.InstanceInfo;

/**
 * 心跳
 * created on 2021/12/8
 * @author klutz
 */
public class BeatInfo {

    private int port;

    private String ip;

    private String serviceName;

    private String instanceId;

    //心跳间隔
    private long period;

    private InstanceInfo instanceInfo;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public InstanceInfo getInstanceInfo() {
        return instanceInfo;
    }

    public void setInstanceInfo(InstanceInfo instanceInfo) {
        this.instanceInfo = instanceInfo;
    }
}
