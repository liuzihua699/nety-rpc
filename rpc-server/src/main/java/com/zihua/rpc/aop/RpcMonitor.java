package com.zihua.rpc.aop;

import com.zihua.rpc.core.ServiceWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author by zihua.
 * create on 2020/04/23.
 * describe: rpc监控器
 */
public class RpcMonitor {
    
    private static final Logger logger = LoggerFactory.getLogger(RpcMonitor.class);

    private volatile static RpcMonitor monitor;
    
    private final Map<String, AtomicInteger> counter = new ConcurrentHashMap<>();
    private final List<RpcLogMsg> mointorList = new CopyOnWriteArrayList<>();
    
    public static RpcMonitor getInstance() {
        if (monitor == null) {
            synchronized (RpcMonitor.class) {
                if (monitor == null) {
                    monitor = new RpcMonitor();    
                }
            }
        }
        return monitor;
    }
    
    public void add(RpcLogMsg logMsg) {
        if (counter.containsKey(logMsg.getClassName())) {
            counter.get(logMsg.getClassName()).getAndIncrement();
        } else {
            counter.put(logMsg.getClassName(), new AtomicInteger(1));
        }
        mointorList.add(logMsg);
    }

    public void print(RpcLogMsg logMsg) {
        logger.info("{}", logMsg);
    }
}
