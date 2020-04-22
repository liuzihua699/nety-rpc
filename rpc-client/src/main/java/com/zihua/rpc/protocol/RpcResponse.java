package com.zihua.rpc.protocol;

import lombok.Data;

import java.util.Objects;

/**
 * @author by 刘子华.
 * create on 2020/4/14.
 * describe: Rpc响应对象（rpc response protocol.）
 */
@Data
public class RpcResponse {

    private static final long serialVersionUID = 5999285200463234265L;
    
    private String requestId;
    private Throwable throwable;
    private Object response;
    
    public Object get() {
        return this.response;
    }

    public boolean isError() {
        return throwable != null;
    }

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
