package com.zihua.rpc.demo.impl;

import com.zihua.rpc.demo.TestHelloService;
import com.zihua.rpc.server.RpcService;

/**
 * Created by 刘子华.
 * hs on 2020/04/17.
 * describe:
 */
@RpcService(TestHelloService.class)
public class TestServiceImpl implements TestHelloService {

    @Override
    public String say() {
        return "HelloImpl implements Hello.";
    }
}
