package com.klutz.registry.server.netty;

import com.klutz.registry.core.entity.ServerInfo;
import com.klutz.registry.server.cluster.NettyClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * netty 客户端 重连
 *
 * created on 2021/12/9
 * @author registry
 */
@ChannelHandler.Sharable
public abstract class ConnectionWatchDog extends ChannelInboundHandlerAdapter implements TimerTask {

    private final Logger logger = LoggerFactory.getLogger(ConnectionWatchDog.class);

    private final Bootstrap bootstrap;
    private final Timer timer;
    private final NettyClient nettyClient;
    private List<ServerInfo> serverList;
    private int index = 0;

    private volatile boolean reconnect = true;
    private int attempts = 0;

    public ConnectionWatchDog(NettyClient nettyClient,Bootstrap bootstrap, Timer timer, List<ServerInfo> serverList) {
        this.nettyClient = nettyClient;
        this.bootstrap = bootstrap;
        this.timer = timer;
        this.serverList = serverList;
    }

    public boolean isReconnect() {
        return reconnect;
    }

    public void setReconnect(boolean reconnect) {
        this.reconnect = reconnect;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if( nettyClient.isStop() ){
            return;
        }
        if( reconnect ) {
            attempts++;
            timer.newTimeout(this, 5, TimeUnit.SECONDS);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        attempts  = 0;
        super.channelActive(ctx);
    }

    private ServerInfo choose( ){
        if( serverList == null || serverList.isEmpty()){
            return null;
        }
        index = Math.abs( (++index) % serverList.size());
        return serverList.get(index);
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        ChannelFuture future;
        synchronized (bootstrap){
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(handlers());
                }
            });
            ServerInfo serverInfo = choose();
            future = bootstrap.connect(serverInfo.getServerIp(),serverInfo.getPort());
        }

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                boolean success = f.isSuccess();
                if( success ){
                    logger.info("reconnect success");
                    nettyClient.setChannel(f.channel());
                }else {
                    logger.info("reconnect error");
                    f.channel().pipeline().fireChannelInactive();
                }
            }
        });

    }


    public abstract ChannelHandler[] handlers();
}
