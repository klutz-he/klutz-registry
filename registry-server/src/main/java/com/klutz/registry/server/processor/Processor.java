package com.klutz.registry.server.processor;

import com.klutz.registry.server.protocol.ProtocolType;

/**
 * created on 2021/12/8
 * @author klutz
 */
public interface Processor {

    ProtocolType protocolType();

    void processor( String body );

}
