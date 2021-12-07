package com.klutz.registry.core.entity;

/**
 * 实例信息
 * created on 2021/12/7
 * @author klutz
 */
public class InstanceInfo {

    private String appName;

    private String ip;

    private String port;

    private String instanceId;

    private Integer expireSeconds;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Integer getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InstanceInfo other = (InstanceInfo) obj;
        if (getInstanceId() == null) {
            if (other.getInstanceId() != null) {
                return false;
            }
        } else if (!getInstanceId().equals(other.getInstanceId())) {
            return false;
        }
        return true;
    }
}
