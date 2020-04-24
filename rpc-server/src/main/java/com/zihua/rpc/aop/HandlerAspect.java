package com.zihua.rpc.aop;

import com.zihua.rpc.core.ServiceWatch;
import com.zihua.rpc.protocol.RpcRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author by 刘子华.
 * create on 2020/04/23.
 * describe: 统计RpcHandler中handler的调用情况
 */
@Aspect
@Component
public class HandlerAspect {
    
    private final static Logger logger = LoggerFactory.getLogger(HandlerAspect.class);

    private ServiceWatch serviceWatch = ServiceWatch.getInstance();
    
    private RpcLogMsg logMsg = new RpcLogMsg();
    
    private long start;

    @Pointcut("execution(* com.zihua.rpc.proxy.CglibObjectProxy.handle(..))")
    public void handle() {

    }

    @Before("handle()")
    public void before(JoinPoint point) throws InterruptedException {
        logMsg.setStart(start = System.currentTimeMillis());
        Object[] args = point.getArgs();
        if (args == null) return;
        if (args[0] instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) args[0];
            
            String     className      = request.getClassName();
            Object     serviceBean    = serviceWatch.getHandler().get(className);
            
            logMsg.setClassName(className);
            logMsg.setRequest(request);
            logMsg.setServiceBean(serviceBean);
        }
    }
    
    @After("handle()")
    public void after(JoinPoint point) {
        long end = System.currentTimeMillis();
        logMsg.setEnd(end);
        logMsg.setRuntime(end - start);
    }
    
    @AfterReturning(pointcut = "handle()", returning = "returning")
    public void doAfterReturn(Object returning) {
        logMsg.setReturning(returning);
        RpcMonitor.getInstance().add(logMsg);
        RpcMonitor.getInstance().print(logMsg);
    }
    
    @AfterThrowing(pointcut = "handle()", throwing = "e")
    public void doAfterThrowing(JoinPoint point, Exception e) {
        long end = System.currentTimeMillis();
        logMsg.setEnd(end);
        logMsg.setThrowable(e);
        RpcMonitor.getInstance().add(logMsg);
        RpcMonitor.getInstance().print(logMsg);
    }
}
