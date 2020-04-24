package com.zihua.rpc.handler;

import com.zihua.rpc.demo.impl.MathServiceImpl;
import com.zihua.rpc.exception.NotFoundInstanceException;
import com.zihua.rpc.protocol.RpcRequest;
import com.zihua.rpc.protocol.RpcResponse;
import com.zihua.rpc.core.RpcServer;
import com.zihua.rpc.proxy.CglibObjectProxy;
import io.netty.channel.*;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author by 刘子华.
 * create on 2020/4/19.
 * describe: RPC Handler.
 */
@Component
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
    
    private static final Logger logger = LoggerFactory.getLogger(RpcHandler.class);

    @Resource
    private CglibObjectProxy proxy;

    private Map<String, Object> handlerMap;
    
    public RpcHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }
    
    public void set(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }
    
    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final RpcRequest request) throws Exception {
        RpcServer.submit(() -> {
            logger.debug("Receive request " + request.getRequestId());
            RpcResponse response = new RpcResponse();
            response.setRequestId(request.getRequestId());
            try {
                Object result = proxy.handle(request);
                response.setResponse(result);
            } catch (Throwable t) {
                response.setThrowable(t);
                logger.error("RPC Server handle request error", t);
            }
            ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    logger.debug("Send response for request " + request.getRequestId());
                }
            });
        });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("server caught exception", cause);
        ctx.close();
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);
    }
}
