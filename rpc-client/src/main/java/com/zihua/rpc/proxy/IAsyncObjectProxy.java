package com.zihua.rpc.proxy;


import com.zihua.rpc.future.DefaultFuture;
import com.zihua.rpc.future.PromiseFuture;
import com.zihua.rpc.future.RpcFuture;

import java.util.concurrent.Future;

/**
 * Created by luxiaoxun on 2016/3/16.
 */
public interface IAsyncObjectProxy {
    public PromiseFuture call(String funcName, Object... args);
}