package com.klutz.registry.server.cluster;

import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.core.entity.ServerInfo;
import com.klutz.registry.server.netty.ConnectionWatchDog;
import com.klutz.registry.server.protocol.Message;
import com.klutz.registry.server.protocol.ProtocolType;
import com.klutz.registry.server.protocol.RegisterDecoder;
import com.klutz.registry.server.protocol.RegisterEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.HashedWheelTimer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2021/12/8.
 * @author klutz
 */
public class NettyClient {

    private List<ServerInfo> serverList;

    private Bootstrap bootstrap;

    private volatile Channel channel;

    private volatile boolean stop = false;

    protected final HashedWheelTimer timer = new HashedWheelTimer();

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);

    public NettyClient(List<ServerInfo> serverList) {
        this.serverList = serverList;
    }

    public void init( ) {
        bootstrap = new Bootstrap();

        ConnectionWatchDog connectionWatchDog = new ConnectionWatchDog(this,bootstrap,timer,serverList) {
            @Override
            public ChannelHandler[] handlers() {
                return new ChannelHandler[]{
                        this,
                        new LengthFieldBasedFrameDecoder(Message.MAX_FRAME_LENGTH,0,4,0,4),
                        new RegisterDecoder(),
                        new RegisterEncoder()
                };
            }
        };

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(connectionWatchDog.handlers());
                    }
                });


        ChannelFuture future = null;
        try {
            synchronized ( bootstrap){
                ServerInfo serverInfo = serverList.get(0);
                future = bootstrap.connect(serverInfo.getServerIp(), serverInfo.getPort());
            }
            future.sync();
            if( future.isSuccess()){
                channel = future.channel();
            }
        }catch (Throwable ex){
            if( future != null ){
                timer.newTimeout(connectionWatchDog,5,TimeUnit.SECONDS);
            }
        }
    }


    public void register(InstanceInfo info) {
        Message message = new Message();
        message.setProtocolType(ProtocolType.REGISTER.getType());
        message.setPayload(info);
        if( channel != null ) {
            channel.writeAndFlush(message);
        }
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void stop( ){
        stop = true;
        timer.stop();
        eventLoopGroup.shutdownGracefully();
    }


    public boolean isStop() {
        return stop;
    }
}
