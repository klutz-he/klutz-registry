package com.klutz.registry.server.service.impl;

import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.server.cluster.RegisterCluster;
import com.klutz.registry.server.service.AbstractPeerAwareInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created on 2021/12/7
 * @author klutz
 */
@Service
public class PeerAwareInstanceRegistryImpl extends AbstractPeerAwareInstanceRegistry {

    @Autowired
    private RegisterCluster registerCluster;

    public enum Action {
        Heartbeat, Register, Cancel;
    }

    @Override
    public void register(InstanceInfo info, boolean isReplication) {
        super.register(info, isReplication);
        if(!isReplication) {
            registerCluster.register(info);
        }
    }
}
