package com.zihua.rpc.proxy;

import com.zihua.rpc.core.ServiceWatch;
import com.zihua.rpc.exception.NotFoundInstanceException;
import com.zihua.rpc.protocol.RpcRequest;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author by zihua.
 * create on 2020/04/23.
 * describe:
 */
@Component
public class CglibObjectProxy {
    
    private static final Logger logger = LoggerFactory.getLogger(CglibObjectProxy.class);

    private ServiceWatch serviceWatch = ServiceWatch.getInstance();

    public Object handle(RpcRequest request) throws Throwable {
        String className   = request.getClassName();
        Object serviceBean = serviceWatch.getHandler().get(className);
        
        if (serviceBean == null) throw new NotFoundInstanceException("not found instance \"" + className + "\".");

        Class<?>   serviceClass   = serviceBean.getClass();
        String     methodName     = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[]   parameters     = request.getParameters();

        logger.debug(serviceClass.getName());
        logger.debug(methodName);
        for (int i = 0; i < parameterTypes.length; ++i) {
            logger.debug(parameterTypes[i].getName());
        }
        for (int i = 0; i < parameters.length; ++i) {
            logger.debug(parameters[i].toString());
        }

        // JDK reflect
        /*Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);*/

        // Cglib reflect
        FastClass serviceFastClass = FastClass.create(serviceClass);
//        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
//        return serviceFastMethod.invoke(serviceBean, parameters);
        // for higher-performance
        int methodIndex = serviceFastClass.getIndex(methodName, parameterTypes);
        return serviceFastClass.invoke(methodIndex, serviceBean, parameters);
    }
    
}
