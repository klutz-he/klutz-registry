package com.klutz.registry.server.protocol;

/**
 * Created on 2021/12/8.
 * @author klutz
 */
public class Message {

    public static final int MAX_FRAME_LENGTH = 10 * 1024 * 1024;

    private int protocolType;

    private Object payload;

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
