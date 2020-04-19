package com.zihua.rpc;

import com.zihua.rpc.registy.ServiceRegistry;
import com.zihua.rpc.server.RpcServer;
import com.zihua.rpc.server.RpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author by 刘子华.
 * create on 2020/04/17.
 * describe: Rpc服务器的启动器
 */
@PropertySource(value = "classpath:rpc-config.properties", ignoreResourceNotFound = true)
@SpringBootApplication
public class ServerApplication {
    
    public static void main(String[] args) {

        SpringApplication.run(ServerApplication.class, args);
//        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
//        ctx.start();
    }
}
