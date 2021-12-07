package com.klutz.registry.server.service;

import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.core.entity.ServiceInfo;

/**
 * created on 2021/12/7
 * @author klutz
 */
public interface PeerAwareInstanceRegistry {

    void register(InstanceInfo instance, boolean isReplication);

    boolean cancel( String appName, String instanceId, boolean isReplication);

    ServiceInfo getServiceInfo(String appName);

    boolean renew(String appName, String instanceId, boolean isReplication);
}
