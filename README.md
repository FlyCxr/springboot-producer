# springboot-producer

1.springboot2.0搭建Rest服务，常见注解，参数接收实例。

2.基于springcloud微服务作为Eureka注册中心服务提供者

3.整合swagger生成API文档，www.gitpig.com:8088/swagger-ui.html#/

4.整合kafka，简单发布订阅Topic

5.整合WebUpload组件实现批量，分片上传，包含进度条，图片预览，以文件为单位续传，www.gitpig.com:8088/webupload/index

6.基于Mysql，集成TkMybatis,pageHelper解放单表DB操作，使用druid连接池，监控管理，www.gitpig.com:8088/druid

7.springboot发送邮件简单示例

8.log4j优化配置，缓冲区，一小时生成一个log文件，优化磁盘IO。分布式可以改造日志同步到kafka

9.集成Jedis,支持单机/集群,封装JedisAPI完成对List，Set，Map，String的常归缓存操作,实现分布式集群锁。

10.利用redis实现单点登录，并限制登录失败次数5次进行账号锁定保护

11.全局配置对cors跨域的支持

12.GlobalEncodingFilter过滤请求，设置编码

13.SecurityInterceptor拦截Controller,校验SIGN TOKEN,延长token有效时间
