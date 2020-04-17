如何设计一个简易的RPC框架？

详图见地址：[地址](https://iblog-zihua.oss-cn-beijing.aliyuncs.com/%E8%AE%BE%E8%AE%A1%E7%AE%80%E6%98%93%E7%9A%84RPC%E6%A1%86%E6%9E%B6_1586879638329.png?x-oss-process=style/iBlog)


# 服务器设计
## 调用链
服务端的调用链如下：
![image.png](https://iblog-zihua.oss-cn-beijing.aliyuncs.com/image_1587121503765.png?x-oss-process=style/iBlog)

## 测试
我们先把服务器跑起来，让客户端构造一个RpcRequest发送给服务端看看效果。

在事件监听处加几个断点，这样可以很清楚的看到rpc类的映射关系和客户端的请求(RpcRequest对象)是什么。

Handler处的断点：

![image.png](https://iblog-zihua.oss-cn-beijing.aliyuncs.com/image_1587121512993.png?x-oss-process=style/iBlog)

RpcServer处的断点：
![image.png](https://iblog-zihua.oss-cn-beijing.aliyuncs.com/image_1587121522765.png?x-oss-process=style/iBlog)

启动SpringBoot后，先经过start断点：

![image.png](https://iblog-zihua.oss-cn-beijing.aliyuncs.com/image_1587121531937.png?x-oss-process=style/iBlog)

可以看到全局维护的handlerMap建立了两个类的映射，K,V关系分别是接口的全名对应接口具体实现实例。

接下来我们尝试构造请求"com.zihua.rpc.demo.TestHelloService"的"say"方法。

![image.png](https://iblog-zihua.oss-cn-beijing.aliyuncs.com/image_1587121543121.png?x-oss-process=style/iBlog)

继续运行

![image.png](https://iblog-zihua.oss-cn-beijing.aliyuncs.com/image_1587121562548.png?x-oss-process=style/iBlog)

可以看到已经成功调用服务端的say方法，并通过响应返回。

客户端会输出"HelloImpl implements Hello."

服务端设计目前可行。


