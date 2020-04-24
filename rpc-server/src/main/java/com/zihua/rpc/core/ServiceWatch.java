package com.zihua.rpc.core;

import com.zihua.rpc.registy.ServiceRegistry;
import io.netty.util.concurrent.DefaultPromise;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author by 刘子华.
 * create on 2020/04/23.
 * describe: rpc服务视察者
 */
public class ServiceWatch extends DefaultPromise {
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceWatch.class);

    private volatile static ServiceWatch serviceProvider;
    
    private ServiceRegistry serviceRegistry;
    private Map<String, Object> handlerMap;

    public void set(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
        setSuccess(handlerMap);
    }
    
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }
    
    // wait for handlerMap initizialer.
    public Map<String, Object> getHandler() throws InterruptedException {
        if (handlerMap == null) {
            sync();
        } 
        return this.handlerMap;
    }

    // 饿汉单例
    public static ServiceWatch getInstance() {
        if (serviceProvider == null) {
            synchronized (ServiceWatch.class) {
                if (serviceProvider == null) {
                    serviceProvider = new ServiceWatch();
                }
            }
        }
        return serviceProvider;
    }
    
    private boolean isRunable() {
        return handlerMap == null;
    }
}
