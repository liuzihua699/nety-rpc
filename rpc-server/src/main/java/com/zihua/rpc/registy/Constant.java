package com.zihua.rpc.registy;

/**
 * ZooKeeper constant
 *
 * @author huangyong
 */
public interface Constant {

    int ZK_SESSION_TIMEOUT = 5000;
    String ZK_REGISTRY_PATH = "/registry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
    int ZK_CONNECTION_TIMEOUT = 50000;
    String ZK_CHILDREN_PATH = ZK_REGISTRY_PATH + "/data";
}
