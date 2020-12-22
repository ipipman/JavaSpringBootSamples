# 分布式服务框架之Dubbo（隐式参数）

### 隐式参数
通过 Dubbo 中的 Attachment 在服务消费方和提供方之间隐式传递参数。
注意：path, group, version, dubbo, token, timeout 几个 key 是保留字段，请使用其它值。

<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/1031608635201_.pic_hd.jpg" width = "620" height = "160" alt="图片名称" align=center />

### Dubbo隐式参数实战
##### 1.客户端通过setAttachment设置隐式参数，在完成下面一次远程调用会被清空，即多次远程调用要多次设置
```java
public class AttachmentConsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/attachment-consumer.xml");
        AttachmentService attachmentService = context.getBean("attachmentService", AttachmentService.class);
        // 隐式传参，后面的远程调用都会隐式将这些参数发送到服务器端，类似cookie
        RpcContext.getContext().setAttachment("index", "1");

        // 业务，远程调用
        // Hello ipman, response from provider: 10.13.224.253:20880, attachment - index: 1
        System.out.println(attachmentService.sayHello("ipman"));
    }
}
```

##### 2.服务端通过getAttachment获取隐式参数
```java
public class AttachmentServiceImpl implements AttachmentService {
    @Override
    public String sayHello(String name) {

        RpcContext context = RpcContext.getContext();
        //获取客户端隐式传入的参数
        String index = (String) context.getAttachment("index");

        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name +
                ", request from consumer: " + context.getRemoteAddress());
        return "Hello " + name + ", response from provider: " + context.getLocalAddress() +
                ", attachment - index: " + index;
    }
}
```