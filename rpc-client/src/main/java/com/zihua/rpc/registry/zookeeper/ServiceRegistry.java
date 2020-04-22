package com.zihua.rpc.registry.zookeeper;

/**
 * 服务注册接口
 */
public interface ServiceRegistry {
    void registry(String data) throws Exception;
}
