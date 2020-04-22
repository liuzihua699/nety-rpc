# netty-rpc

一个简单的RPC实现，基于Spring, Zookeeper, Netty

## 正在开发

- 异常处理 √
- AOP埋点监控
- 集成断路器
- 支持跨平台
- 提供API文档支持,Restful调用
- 更高的性能...

## 架构设计

### 服务端

![image.png](https://iblog-zihua.oss-cn-beijing.aliyuncs.com/image_1587534981067.png?x-oss-process=style/iBlog)

### 客户端

![image.png](https://iblog-zihua.oss-cn-beijing.aliyuncs.com/image_1587535011284.png?x-oss-process=style/iBlog)

## 怎么使用

1.  创建一个RPC接口

```java
public interface MathService {
    public int max(int a, int b);
}
```

2.  实现该接口并加上@RpcService注解

```java
@RpcService(MathService.class)
public class MathServiceImpl implements MathService {

    @Override
    public int max(int a, int b) {
        return Math.max(a, b);
    }
}
```

3.  运行zookeeper

```java
1. 官网下载最新的releases版本(源码版本是无法运行的) https://zookeeper.apache.org/releases.html
2. 运行zoopeeker，比如zookeeper默认运行在127.0.0.1:2181上
```

4.  启动服务

```java
1. 在rpc-config.properties中修改rpc服务地址和zoopeeker地址
2. 启动ServerApplication即可
```

5.  使用客户端

```java
ServerDiscovery discovery = new ServerDiscovery("127.0.0.1:2181");
rpcClient rpcClient = new RpcClient(serviceDiscovery);

MathService mathService = rpcClient.create(MathService.class);
System.out.println("最大值：" + mathService.max(233, 19));

rpcClient.stop();
```

## 注意

1. 发起调用的接口须与服务端路径一致
```
比如服务端实例接口路径是"com.zihua.rpc.demo.MathService"
那么客户端就在"com.zihua.rpc.demo"下创建MathService
```

2. 如果路径不一致的情况下使用@MarkClassName设置包路径
```
比如客户端类的路径是"com.zihua.rpc.MathService"
服务端的路径是"com.zihua.rpc.demo.MathService"
加上@MarkClassName注解后可以手动更正
```
```java
@MarkClassName(className = "com.zihua.rpc.demo.MathService")
public interface MathService {
    public int max(int a, int b);
}
```
```java
ServerDiscovery discovery = new ServerDiscovery("127.0.0.1:2181");
rpcClient rpcClient = new RpcClient(serviceDiscovery);

MathService mathService = rpcClient.create(MathService.class);
System.out.println("最大值：" + mathService.max(233, 19));

rpcClient.stop();
```