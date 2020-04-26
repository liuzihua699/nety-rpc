package com.zihua.rpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author by 刘子华.
 * create on 2020/04/26.
 * describe:
 */
@Component
@ConfigurationProperties(prefix = "rpc-config", ignoreUnknownFields = true)
public class RpcConfig {
    
    private String zookeeperHost;
    private Integer zookeeperPort;
    private String host;
    private Integer port;

    public String getZookeeperHost() {
        return zookeeperHost;
    }

    public void setZookeeperHost(String zookeeperHost) {
        this.zookeeperHost = zookeeperHost;
    }

    public Integer getZookeeperPort() {
        return zookeeperPort;
    }

    public void setZookeeperPort(Integer zookeeperPort) {
        this.zookeeperPort = zookeeperPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}


