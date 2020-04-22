package com.zihua.rpc.future;


import com.zihua.rpc.protocol.RpcRequest;
import com.zihua.rpc.protocol.RpcResponse;
import com.zihua.rpc.proxy.IAsyncObjectProxy;

/**
 * @description: 自定义实现Future
 *
 * @author: pjmike
 * @create: 2019/04/07 16:34
 */
public class DefaultFuture {
    
    private RpcRequest request;
    private RpcResponse response;
    private volatile boolean isSucceed = false;
    private final Object object = new Object();

    public DefaultFuture(RpcRequest rpcRequest) {
        this.request = rpcRequest;
    }

    public Object get() {
        synchronized (object) {
            while (!isSucceed) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return this.response.getResponse();
        }
    }

    public void set(RpcResponse response) {
        if (isSucceed) {
            return;
        }
        synchronized (object) {
            this.response = response;
            this.isSucceed = true;
            object.notify();
        }
    }
}
