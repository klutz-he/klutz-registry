package com.klutz.registry.server.protocol;

import com.klutz.registry.server.processor.Dispatcher;
import com.klutz.registry.server.utils.SpringContextUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class RegisterDecoder extends ByteToMessageDecoder {

    private final Logger logger = LoggerFactory.getLogger(RegisterDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int type = in.readInt();
        CharSequence charSequence = in.readCharSequence(in.readableBytes(), Charset.defaultCharset());
        ProtocolType protocolType = ProtocolType.valueOf(type);
        if( protocolType == null ){
            logger.error("unknown protocol type {}",type);
            return;
        }

        String body = charSequence.toString();
        Dispatcher dispatcher = SpringContextUtil.getBean(Dispatcher.class);
        dispatcher.process(protocolType,body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //inbound 异常会进来
        //不打印异常堆栈
        logger.error("exceptionCaught {}",cause.getMessage());
    }
}
