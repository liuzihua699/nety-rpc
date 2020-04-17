package com.zihua.rpc.protocol;

import lombok.Data;

import java.util.Objects;

/**
 * Created by 刘子华.
 * hs on 2020/04/14.
 * describe: Rpc响应对象
 */
@Data
public class RpcResponse {

    private static final long serialVersionUID = 5999285200463234265L;
    
    private String requestId;
    private Throwable throwable;
    private Object response;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RpcResponse that = (RpcResponse) o;

        if (!Objects.equals(requestId, that.requestId)) return false;
        if (!Objects.equals(throwable, that.throwable)) return false;
        return Objects.equals(response, that.response);
    }
}
