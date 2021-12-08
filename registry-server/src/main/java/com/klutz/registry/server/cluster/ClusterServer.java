package com.klutz.registry.server.cluster;

import com.klutz.registry.server.processor.Processor;
import com.klutz.registry.server.protocol.ProtocolType;
import com.klutz.registry.server.protocol.RegisterDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created on 2021/12/8
 * @author klutz
 */
@Component
public class ClusterServer {

    public static final int MAX_FRAME_LENGTH = 10 * 1024 * 1024;

    @Value("${cluster.port:9527}")
    private Integer clusterPort;

    private ServerBootstrap serverBootstrap;

    @Autowired
    private List<Processor> processors;

    private Map<ProtocolType,Processor> processorMap = new HashMap<>();

    @PostConstruct
    public void init() throws Exception{

        for( Processor processor : processors ){
            processorMap.put(processor.protocolType(),processor);
        }
        serverBootstrap = new ServerBootstrap()
                .group(new NioEventLoopGroup(1),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(MAX_FRAME_LENGTH,0,4,0,4));
                        socketChannel.pipeline().addLast(new RegisterDecoder(processorMap));
                    }
                });
        ChannelFuture f = serverBootstrap.bind(clusterPort).sync();
    }



}
