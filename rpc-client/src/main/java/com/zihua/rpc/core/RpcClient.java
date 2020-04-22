package com.zihua.rpc.core;

import com.zihua.rpc.proxy.IAsyncObjectProxy;
import com.zihua.rpc.proxy.ObjectProxy;
import com.zihua.rpc.registry.ServiceDiscovery;

import java.lang.reflect.Proxy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author by zihua.
 * create on 2020/04/21.
 * describe:
 */
public class RpcClient {
    private String serverAddress;
    private ServiceDiscovery serviceDiscovery;
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16,
            600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    public RpcClient(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RpcClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<T>(interfaceClass)
        );
    }

    // TODO 这里逻辑是强制关闭zookeeper，待改动
    public void stop() {
        threadPoolExecutor.shutdown();
//        serviceDiscovery.stop();
        ConnectManage.getInstance().stop();
    }

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }
}
