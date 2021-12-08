package com.klutz.registry.core.lifecycle;

/**
 * created on 2021/12/8
 * @author klutz
 */
public interface Closeable {

    void shutdown() throws Exception;
    
}
