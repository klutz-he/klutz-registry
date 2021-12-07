package com.klutz.registry.core.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * created on 2021/12/7
 * @author klutz
 */
public class ServiceInfo {

    private String name;

    private List<InstanceInfo> instances;

    public ServiceInfo(String name) {
        this.name = name;
        this.instances = new ArrayList<>();
    }

    public void addInstance(InstanceInfo instance) {
        instances.add(instance);
    }

    public String getName() {
        return name;
    }

    public List<InstanceInfo> getInstances() {
        return instances;
    }
}
