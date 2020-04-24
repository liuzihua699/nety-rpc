package com.zihua.rpc.protocol;

import lombok.Data;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author by 刘子华.
 * create on 2020/04/14.
 * describe: Rpc请求对象（rpc request protocol.）
 */
@Data
public class RpcRequest {
    
    private static final long serialVersionUID = -3860389620287914106L;
    
    private String requestId;
    private String className;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] parameterTypes;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RpcRequest that = (RpcRequest) o;

        if (!Objects.equals(requestId, that.requestId)) return false;
        if (!Objects.equals(className, that.className)) return false;
        if (!Objects.equals(methodName, that.methodName)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(parameters, that.parameters)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        
        return Arrays.equals(parameterTypes, that.parameterTypes);
    }
}
