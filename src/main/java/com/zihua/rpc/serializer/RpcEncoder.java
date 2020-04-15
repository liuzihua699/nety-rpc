package com.zihua.rpc.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by 刘子华.
 * hs on 2020/04/15.
 * describe: RPC编码器
 */
public class RpcEncoder extends MessageToByteEncoder {
    
    private Class<?> clazz;
    private Serialization serialization;

    public RpcEncoder(Class<?> clazz, Serialization serialization) {
        this.clazz = clazz;
        this.serialization = serialization;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
        if (null != clazz && clazz.isInstance(msg)) {
            byte[] bytes = serialization.serialize(msg);
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }
}
