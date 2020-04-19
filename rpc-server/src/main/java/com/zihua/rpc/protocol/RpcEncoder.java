package com.zihua.rpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author by 刘子华.
 * create on 2020/4/15.
 * describe: RPC编码器，RpcRequest->BytesStream
 */
public class RpcEncoder extends MessageToByteEncoder {
    
    private Class<?> clazz;

    public RpcEncoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf byteBuf) throws Exception {
        if (clazz.isInstance(msg)) {
//            byte[] bytes = serialization.serialize(msg);
            byte[] bytes = SerializationUtil.serialize(msg);
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }
}
