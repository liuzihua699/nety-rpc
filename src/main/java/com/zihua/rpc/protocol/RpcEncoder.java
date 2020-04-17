package com.zihua.rpc.protocol;

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

    public RpcEncoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
        if (null != clazz && clazz.isInstance(msg)) {
//            byte[] bytes = serialization.serialize(msg);
            byte[] bytes = SerializationUtil.serialize(msg);
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }
}
