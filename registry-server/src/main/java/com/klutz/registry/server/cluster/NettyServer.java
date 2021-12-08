package com.klutz.registry.server.cluster;

import com.klutz.registry.server.processor.Processor;
import com.klutz.registry.server.protocol.Message;
import com.klutz.registry.server.protocol.ProtocolType;
import com.klutz.registry.server.protocol.RegisterDecoder;
import com.klutz.registry.server.protocol.RegisterEncoder;
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
public class NettyServer {


    @Value("${cluster.port}")
    private Integer clusterPort;

    private ServerBootstrap serverBootstrap;

    @PostConstruct
    public void init() throws Exception{


        serverBootstrap = new ServerBootstrap()
                .group(new NioEventLoopGroup(1),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Message.MAX_FRAME_LENGTH,0,4,0,4));
                        socketChannel.pipeline().addLast(new RegisterDecoder());
                        socketChannel.pipeline().addLast(new RegisterEncoder());
                    }
                });
        ChannelFuture f = serverBootstrap.bind(clusterPort).sync();
    }



}
