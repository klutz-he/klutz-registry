package com.klutz.registry.client;

import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.core.exception.KlutzException;
import com.klutz.registry.core.lifecycle.Closeable;

/**
 * 注册 拉取
 * created on 2021/12/9
 * @author klutz
 */
public interface KlutzClient extends Closeable {

    void registerInstance( InstanceInfo instance) throws KlutzException;

    void shutdown( ) throws Exception;

    void deregister( InstanceInfo instanceInfo);

}
