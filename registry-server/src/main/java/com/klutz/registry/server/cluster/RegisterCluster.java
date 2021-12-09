package com.klutz.registry.server.cluster;

import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.core.entity.ServerInfo;
import com.klutz.registry.core.utils.ClusterUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created on 2021/12/8
 * @author klutz
 */
@Component
public class RegisterCluster {

    @Value("${cluster.nodes}")
    private String nodes;

    private List<NettyClient> nettyClients =new ArrayList<>();

    @PostConstruct
    public void init() throws Exception{
        String[] split = nodes.split(",");
        for( String node : split){
            ServerInfo serverInfo = ClusterUtils.resolve(node);
            nettyClients.add(new NettyClient(Collections.singletonList(serverInfo)));
        }

        for( NettyClient nettyClient : nettyClients){
            nettyClient.init();
        }
    }

    public void register(InstanceInfo info) {
        for( NettyClient nettyClient : nettyClients){
            nettyClient.register(info);
        }
    }
}
