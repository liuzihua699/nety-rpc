package com.zihua.rpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

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
