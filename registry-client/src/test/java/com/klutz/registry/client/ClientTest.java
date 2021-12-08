package com.klutz.registry.client;

import com.klutz.registry.core.entity.InstanceInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class ClientTest {

    private RegistryClient registryClient;

    @Before
    public void init( ){

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR,"http://127.0.0.1:8081");
        registryClient = new RegistryClient(properties);
    }

    @Test
    public void register() throws Exception{
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setInstanceId("xx-001");
        instanceInfo.setAppName("xx");
        instanceInfo.setIp("127.0.0.1");
        instanceInfo.setPort("8085");
        instanceInfo.setExpireSeconds(10);

        registryClient.registerInstance(instanceInfo);

        TimeUnit.SECONDS.sleep(100);
    }

}
