package com.zihua.rpc.demo.impl;

import com.zihua.rpc.demo.MathService;
import com.zihua.rpc.server.RpcService;

/**
 * Created by 刘子华.
 * hs on 2020/04/17.
 * describe:
 */
@RpcService(MathService.class)
public class MathServiceImpl implements MathService {

    @Override
    public int max(int a, int b) {
        return Math.max(a, b);
    }
}
