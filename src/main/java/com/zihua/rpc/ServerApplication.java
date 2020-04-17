package com.zihua.rpc;

import com.zihua.rpc.server.RpcServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 刘子华.
 * hs on 2020/04/17.
 * describe:
 */
@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        ctx.start();
        System.out.println(ctx.getBean(RpcServer.class));
    }
}
