package com.zihua.rpc.serializer;

import java.io.IOException;

/**
 * Created by 刘子华.
 * hs on 2020/04/15.
 * describe: 序列化协议定义
 */
public interface Serialization {

    /**
     *  序列化: 对象 -> 字节流
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     *  反序列化: 字节流 -> 对象
     */
    <T> T deSerialize(Class<T> clazz, byte[] bytes) throws IOException;
}
