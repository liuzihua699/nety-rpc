package com.zihua.rpc.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Created by 刘子华.
 * hs on 2020/04/15.
 * describe: 序列化协议的默认实现，使用jackson。
 */
public class JSONSerialization implements Serialization {
    
    private ObjectMapper objectMapper;

    public JSONSerialization() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public <T> byte[] serialize(T obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T deSerialize(Class<T> clazz, byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        Serialization s = new JSONSerialization();
        System.out.println(s.serialize("hello world"));
        
    }
}
