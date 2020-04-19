package com.zihua.rpc.protocol;

import java.io.IOException;

/**
 * @author by 刘子华.
 * create on 2020/4/15.
 * describe: 序列化接口的定义
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
