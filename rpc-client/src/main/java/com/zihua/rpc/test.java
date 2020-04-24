package com.zihua.rpc;

import com.zihua.rpc.core.RpcClient;
import com.zihua.rpc.registry.ServiceDiscovery;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author by 刘子华.
 * create on 2020/04/21.
 * describe:
 */
public class test {
    public static void main(String[] args) {
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery("127.0.0.1:2181");
        RpcClient rpcClient = new RpcClient(serviceDiscovery);
        
        MathService mathService = rpcClient.create(MathService.class);
        System.out.println("最大值：" + mathService.max(233, 19));

        rpcClient.stop();
    }
}
 