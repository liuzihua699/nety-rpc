package com.zihua.rpc;

import com.zihua.rpc.core.MarkClassName;

/**
 * Created by 刘子华.
 * hs on 2020/04/17.
 * describe:
 */
@MarkClassName(className = "com.zihua.rpc.demo.MathService")
public interface MathService {
    public int max(int a, int b);
}
