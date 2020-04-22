package com.zihua.rpc.future;

/**
 * Created by luxiaoxun on 2016-03-17.
 */
public interface AsyncRpcCallback {

    void success(Object result);

    void fail(Exception e);
}
