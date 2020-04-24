package com.zihua.rpc.handler;

import com.zihua.rpc.core.ServiceWatch;
import com.zihua.rpc.protocol.RpcRequest;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by zihua.
 * create on 2020/04/23.
 * describe: 处理http请求，响应rpc调用数据
 */
public class WatchHandler extends ChannelHandlerAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(WatchHandler.class);

    private ServiceWatch serviceWatch = ServiceWatch.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        HttpRequest request = null;
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
        }

        System.out.println(request);
    }
}
