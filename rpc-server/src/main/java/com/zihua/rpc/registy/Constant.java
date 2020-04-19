package com.zihua.rpc.registy;

/**
 * @author by 刘子华.
 * create on 2020/4/17.
 * describe:  注册中心常量表
 */
public interface Constant {

    int ZK_SESSION_TIMEOUT = 5000;

    String ZK_REGISTRY_PATH = "/registry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
}
