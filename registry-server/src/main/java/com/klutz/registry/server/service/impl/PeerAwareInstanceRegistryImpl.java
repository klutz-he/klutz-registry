package com.klutz.registry.server.service.impl;

import com.klutz.registry.server.service.AbstractPeerAwareInstanceRegistry;
import org.springframework.stereotype.Service;

/**
 * created on 2021/12/7
 * @author klutz
 */
@Service
public class PeerAwareInstanceRegistryImpl extends AbstractPeerAwareInstanceRegistry {

    public enum Action {
        Heartbeat, Register, Cancel;
    }

}
