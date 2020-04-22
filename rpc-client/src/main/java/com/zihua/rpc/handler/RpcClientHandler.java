package com.zihua.rpc.handler;

import com.zihua.rpc.future.DefaultFuture;
import com.zihua.rpc.future.PromiseFuture;
import com.zihua.rpc.future.RpcFuture;
import com.zihua.rpc.protocol.RpcRequest;
import com.zihua.rpc.protocol.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * @aruhor by zihua.
 * create on 2020/4/22.
 * describe: 
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    private ConcurrentHashMap<String, PromiseFuture> pendingRPC = new ConcurrentHashMap<>();

    private volatile Channel channel;
    private SocketAddress remotePeer;

    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getRemotePeer() {
        return remotePeer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        String requestId = response.getRequestId();
        PromiseFuture rpcFuture = pendingRPC.get(requestId);
        if (null != rpcFuture) {
            pendingRPC.remove(requestId);
            rpcFuture.setSuccess(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client caught exception", cause);
        ctx.close();
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                future.channel().close();
            }
        });
    }

    /**
     * @author by zihua.
     * create on 2020/4/21.
     * describe: 发送请求
     */
    public PromiseFuture send(RpcRequest request) {
        final CountDownLatch latch = new CountDownLatch(1);

        PromiseFuture rpcFuture = new PromiseFuture(request);
        pendingRPC.put(request.getRequestId(), rpcFuture);

        // 往channel中异步发送请求
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        return rpcFuture;
    }
}
