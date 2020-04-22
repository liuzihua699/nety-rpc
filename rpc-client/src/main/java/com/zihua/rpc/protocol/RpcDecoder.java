package com.zihua.rpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author by 刘子华.
 * create on 2020/04/15.
 * describe: RPC解码器，BytesStream->RpcRequest
 */
public class RpcDecoder extends ByteToMessageDecoder {
    
    private Class<?> clazz;

    public RpcDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        
        // 标记当前读的位置
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        // 将byteBuf中的数据读入data字节数组
        byteBuf.readBytes(data);
//        Object obj = serialization.deSerialize(clazz, data);
        Object obj = SerializationUtil.deserialize(data, clazz);
        list.add(obj);
    }
}
