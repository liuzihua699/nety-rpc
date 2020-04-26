package com.zihua.rpc.core;

import com.zihua.rpc.handler.RpcHandler;
import com.zihua.rpc.handler.WatchHandler;
import com.zihua.rpc.protocol.RpcDecoder;
import com.zihua.rpc.protocol.RpcEncoder;
import com.zihua.rpc.protocol.RpcRequest;
import com.zihua.rpc.protocol.RpcResponse;
import com.zihua.rpc.registy.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author by 刘子华.
 * create on 2020/04/17.
 * describe: 服务器主要处理逻辑，RpcServer->RpcHandler
 */
@Component
public class RpcServer implements ApplicationContextAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    @Value("${rpc-config.host}")
    private String host;
    
    @Value("${rpc-config.port}")
    private Integer port;
    
    @Resource
    private ServiceRegistry serviceRegistry;

    @Resource
    private RpcHandler rpcHandler;

    private ServiceWatch serviceWatch = ServiceWatch.getInstance();

    private Map<String, Object> handlerMap = new ConcurrentHashMap<>();
    private static ThreadPoolExecutor threadPoolExecutor;

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    
    static {
        // 停机处理
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("程序退出！！！");
        }));
    }

    public RpcServer() {
    }

    public RpcServer(String host, Integer port, ServiceRegistry serviceRegistry) {
        this.host = host;
        this.port = port;
        this.serviceRegistry = serviceRegistry;
    }

    /**
     *  装配所有的 @RpcService 实例
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                logger.info("Loading service: {}", interfaceName);
                handlerMap.put(interfaceName, serviceBean);
            }
        }
        rpcHandler.set(handlerMap);
    }
    
    /**
     *  装配完成后，启动rpc服务
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
    
    // 启动netty，监听端口，通道内拦截rpc请求和http请求
    public void start() throws Exception {
        if (bossGroup == null && workerGroup == null) {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    .addLast(rpcHandler);
//                                    .addLast(new HttpResponseEncoder())
//                                    .addLast(new HttpRequestDecoder())
//                                    .addLast(new WatchHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String serverAddress = host + ":" + port;

            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.info("Server started on host [{}] port [{}].",host, port);

            // 注册至zookeeper
            if (serviceRegistry != null) {
                serviceRegistry.register(serverAddress);
            }
            
            // 设置视察者
            serviceWatch.set(handlerMap);
            serviceWatch.setServiceRegistry(serviceRegistry);

            future.channel().closeFuture().sync();
            logger.info("Server stop!");
        }
    }


    public void stop() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    /**
     *  提交任务
     */
    public static void submit(Runnable task) {
        if (threadPoolExecutor == null) {
            synchronized (RpcServer.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
                            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
                }
            }
        }
        threadPoolExecutor.submit(task);
    }

    /**
     *  添加服务提供者
     */
    public RpcServer addService(String interfaceName, Object serviceBean) {
        if (!handlerMap.containsKey(interfaceName)) {
            logger.info("Loading service: {}", interfaceName);
            handlerMap.put(interfaceName, serviceBean);
        }
        return this;
    }
}
