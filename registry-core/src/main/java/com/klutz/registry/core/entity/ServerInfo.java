package com.klutz.registry.core.entity;

/**
 * created on 2021/12/9
 * @author klutz
 */
public class ServerInfo {

    private String serverIp;

    private Integer port;

    public ServerInfo(String serverIp, Integer port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
