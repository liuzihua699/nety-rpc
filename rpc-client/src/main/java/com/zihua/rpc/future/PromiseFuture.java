package com.zihua.rpc.future;

import com.zihua.rpc.protocol.RpcRequest;
import com.zihua.rpc.protocol.RpcResponse;
import io.netty.util.concurrent.DefaultPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author by 刘子华.
 * create on 2020/04/22.
 * describe:
 */
public class PromiseFuture extends DefaultPromise<RpcResponse> {

    private static final Logger logger = LoggerFactory.getLogger(PromiseFuture.class);
    
    private RpcRequest request;

    public PromiseFuture(RpcRequest request) {
        this.request = request;
    }

    public RpcResponse done() throws ExecutionException, InterruptedException {
//        RpcResponse response = super.get();
//        if (response != null) {
//            return response.getResponse();
//        } else {
//            return null;
//        }
        return super.get();
    }

    public static void main(String[] args) throws InterruptedException {
        // 使用方法：
        // 1、使用sync()阻塞等待异步回调
        // future.sync();
        // 2、异步完成后使用setSuccess()返回
        // future.setSuccess(response)
    }
}
