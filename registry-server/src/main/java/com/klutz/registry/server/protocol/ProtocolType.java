package com.klutz.registry.server.protocol;

/**
 * created on 2021/12/8
 * @author klutz
 */
public enum ProtocolType {

    REGISTER(101),
    REGISTER_RESPONSE(102),

    RENEW(103),
    RENEW_RESPONSE(104),

    ;

    private int type;

    ProtocolType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ProtocolType valueOf(int type){
        for( ProtocolType protocolType : ProtocolType.values()){
            if( protocolType.getType() == type){
                return protocolType;
            }
        }
        return null;
    }


}
