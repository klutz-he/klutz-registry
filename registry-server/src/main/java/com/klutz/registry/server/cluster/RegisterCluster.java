package com.klutz.registry.server.cluster;

import com.klutz.registry.core.entity.InstanceInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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
            String[] ipAndPort = node.split(":");
            nettyClients.add(new NettyClient(ipAndPort[0],Integer.valueOf(ipAndPort[1])));
        }

        for( NettyClient nettyClient : nettyClients){
            nettyClient.init();
            nettyClient.connect();
        }
    }

    public void register(InstanceInfo info) {
        for( NettyClient nettyClient : nettyClients){
            nettyClient.register(info);
        }
    }
}
