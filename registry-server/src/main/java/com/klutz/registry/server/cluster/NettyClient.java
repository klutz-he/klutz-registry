package com.klutz.registry.server.cluster;

import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.server.protocol.Message;
import com.klutz.registry.server.protocol.ProtocolType;
import com.klutz.registry.server.protocol.RegisterDecoder;
import com.klutz.registry.server.protocol.RegisterEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2021/12/8.
 * @author klutz
 */
public class NettyClient {

    private String ip;

    private Integer port;

    private Bootstrap bootstrap;

    private Channel channel;

    private ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("netty.client");
            return thread;
        }
    });

    public NettyClient(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public void init( ) {
        bootstrap = new Bootstrap();

        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Message.MAX_FRAME_LENGTH,0,4,0,4));
                        socketChannel.pipeline().addLast(new RegisterDecoder());
                        socketChannel.pipeline().addLast(new RegisterEncoder());
                    }
                });
    }

    public void connect( ) throws Exception{

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(50);
                    ChannelFuture sync = bootstrap.connect(ip, port).sync();
                    channel = sync.channel();
                }catch (Exception ex){

                }
            }
        });

    }

    public void register(InstanceInfo info) {
        Message message = new Message();
        message.setProtocolType(ProtocolType.REGISTER.getType());
        message.setPayload(info);
        if( channel != null ) {
            channel.writeAndFlush(message);
        }
    }
}
