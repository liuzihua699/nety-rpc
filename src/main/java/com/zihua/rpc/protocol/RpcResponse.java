package com.zihua.rpc.protocol;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by 刘子华.
 * hs on 2020/04/14.
 * describe: Rpc响应对象
 */
@Data
@Builder(toBuilder = true)
public class RpcResponse implements Serializable{

    private static final long serialVersionUID = 5999285200463234265L;
    
    private String requestId;
    private Throwable throwable;
    private Object response;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RpcResponse that = (RpcResponse) o;

        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (throwable != null ? !throwable.equals(that.throwable) : that.throwable != null) return false;
        return response != null ? response.equals(that.response) : that.response == null;
    }
}
