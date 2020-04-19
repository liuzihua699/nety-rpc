package com.zihua.rpc.server;

import com.zihua.rpc.demo.impl.MathServiceImpl;
import com.zihua.rpc.protocol.RpcRequest;
import com.zihua.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author by 刘子华.
 * create on 2020/4/19.
 * describe: RPC Handler.
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
    
    private static final Logger logger = LoggerFactory.getLogger(RpcHandler.class);

    private final Map<String, Object> handlerMap;

    /**
     * Created by 刘子华.
     * hs on 2020/4/19.
     * describe: 
     */
    public RpcHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final RpcRequest request) throws Exception {
        RpcServer.submit(new Runnable() {
            @Override
            public void run() {
                logger.debug("Receive request " + request.getRequestId());
                RpcResponse response = new RpcResponse();
                response.setRequestId(request.getRequestId());
                try {
                    Object result = handle(request);
                    response.setResponse(result);
                } catch (Throwable t) {
                    System.out.println("方法调用出现异常");
                    response.setThrowable(t);
                    logger.error("RPC Server handle request error", t);
                }
                ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        logger.debug("Send response for request " + request.getRequestId());
                    }
                });
            }
        });
    }

    private Object handle(RpcRequest request) throws Throwable {
        String className   = request.getClassName();
        Object serviceBean = handlerMap.get(className);

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

    public static void main(String[] args) throws InvocationTargetException {
        try {
            test();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("报错了");
        }
    }
    
    
    public static void test() throws Throwable {
        String className   = "com.zihua.rpc.demo.MathServiceImpl";
        Object serviceBean = new MathServiceImpl();

        Class<?>   serviceClass   = serviceBean.getClass();
        String     methodName     = "max";

//        方法有参数的请求构造
//        Class<?>[] parameterTypes = new Object[]{92, 325};
//        Object[]   parameters     = new Class[2]{int.class, int.class};

//        方法没有参数的请求构造
        Class<?>[] parameterTypes = null;
        Object[]   parameters = null;

        FastClass serviceFastClass = FastClass.create(serviceClass);
        int methodIndex = serviceFastClass.getIndex(methodName, parameterTypes);
        Object result = serviceFastClass.invoke(methodIndex, serviceBean, parameters);
        System.out.println(result);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("server caught exception", cause);
        ctx.close();
    }
}
