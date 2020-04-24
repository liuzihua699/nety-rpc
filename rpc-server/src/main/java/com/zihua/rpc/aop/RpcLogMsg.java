package com.zihua.rpc.aop;

import com.zihua.rpc.protocol.RpcRequest;
import com.zihua.rpc.protocol.RpcResponse;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;

/**
 * @author by 刘子华.
 * create on 2020/04/23.
 * describe:
 */
@Data
public class RpcLogMsg {

    private String className;
    private RpcRequest request;
    private Object returning;
    private Object serviceBean;
    private long start;
    private long end;
    private long runtime;
    private Throwable throwable;
}
