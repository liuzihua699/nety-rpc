package com.zihua.rpc.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by 刘子华.
 * hs on 2020/04/15.
 * describe: RPC解码器
 */
public class RpcDecoder extends ByteToMessageDecoder {
    
    private Class<?> clazz;
    private Serialization serialization;

    public RpcDecoder(Class<?> clazz, Serialization serialization) {
        this.clazz = clazz;
        this.serialization = serialization;
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
        Object obj = serialization.deSerialize(clazz, data);
        list.add(obj);
    }
}
