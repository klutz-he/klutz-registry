package com.klutz.registry.server.protocol;

import com.klutz.registry.core.utils.JacksonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * Created on 2021/12/8.
 * @author klutz
 */
public class RegisterEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        String payload = JacksonUtils.toJson(message.getPayload());
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
        int byteLen = bytes.length + 4;
        byteBuf.writeInt(byteLen);
        byteBuf.writeInt(message.getProtocolType());
        byteBuf.writeBytes(bytes);
    }
}
