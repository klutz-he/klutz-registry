package com.klutz.registry.server.processor;

import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.core.utils.JacksonUtils;
import com.klutz.registry.server.protocol.ProtocolType;
import com.klutz.registry.server.service.PeerAwareInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PeerAwareInstanceRegistry peerAwareInstanceRegistry;

    @Override
    public void processor(String body) {
        peerAwareInstanceRegistry.register(JacksonUtils.readValue(body, InstanceInfo.class),false);
    }
}
