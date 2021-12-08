package com.klutz.registry.server.processor;

import com.klutz.registry.server.protocol.ProtocolType;
import org.springframework.stereotype.Component;

/**
 * created on 2021/12/8
 * @author klutz
 */
@Component
public class RegisterProcessor implements Processor{

    @Override
    public ProtocolType protocolType() {
        return ProtocolType.REGISTER;
    }

    @Override
    public void processor(String body) {

    }
}
