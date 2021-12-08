package com.klutz.registry.server.protocol;

import com.klutz.registry.server.processor.Dispatcher;
import com.klutz.registry.server.processor.Processor;
import com.klutz.registry.server.utils.SpringContextUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
