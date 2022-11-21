<h1 align="center">推送消息</h1>

**<span style="color:red">本项目是在</span> https://github.com/yindz/message-center <span style="color:red">上进行的修改</span>**

### 概述

#### 特点

- 实现了邮件推送、短信推送和APP消息推送
- 适配了极光推送、个推、阿里云短信、腾讯云短信、云片短信等第三方云服务，开箱即用
- 支持本地eventBus消息队列框架和kafka消息队列框架，RabbitMQ、RocketMQ还在开发

#### 核心流程

![Untitled Diagram](https://raw.githubusercontent.com/isIvanTsui/img/master/Untitled%20Diagram.png)

> 生产者向`MQ`中投递消息，消费端订阅了该队列，接收到消息后，调用第三方服务商的`API`去真正的推送消息

![image-20221121160116682](https://raw.githubusercontent.com/isIvanTsui/img/master/image-20221121160116682.png)

#### 使用

```java
@RestController
public class MsgController {
    @Autowired
    private EmailMessageProducer emailMessageProducer;

    @GetMapping("test_email")
    public void testEmail() {
        EmailMessage message = new EmailMessage();
        message.setMessageId("12352453");
        message.setMessageType("email");
        message.setReceiver("428275252@qq.com");
        message.setSubject("测试标题");
        message.setHtmlContent(true);
        message.setContent("这是一封测试邮件");
        emailMessageProducer.send(message);
    }
}
```

> `AppMessageProducer`提供向`APP`发送消息的方法
>
> `EmailMessageProducer`提供向邮箱发送邮件的方法
>
> `SmsMessageProducer`提供向手机发送短信的方法

-----



### 支持的服务商官网

#### 短信

- 阿里云：https://www.aliyun.com/product/sms

- 腾讯云：https://cloud.tencent.com/product/sms
- 云片：https://www.yunpian.com/

#### APP

- 极光推送：https://www.jiguang.cn/

- 个推：https://www.getui.com/cn/index.html

-----



### 配置说明

#### application.yaml

```yaml
spring:
  #kafka相关配置
  kafka:
    bootstrap-servers: localhost:9092
  #邮件相关配置
  mail:
    host: smtp.qq.com
    port: 465
    username: yfcui@foxmail.com
    password: sgowrdnbvoefbhjd
    default-encoding: UTF-8
    protocol: smtps

msg:
  #采用哪种MQ，可选择[eventBus]、[kafka]
  mq-type: eventBus

  #email推送
  email-enabled: true

  #App消息推送(只能选择启用一种)
  jpush-enabled: false #极光推送
  jpush-app-key: asdfghjkl
  jpush-master-secret: poiuytrewq

  getui-enabled: false #个推
  getui-app-id: 0987654321
  getui-url: http://example.com
  getui-master-secret: zxcvbnm
  getui-app-key: 96854544

  #短信推送(只能选择启用一种)
  qcloud-sms-enabled: false #腾讯云短信
  qcloud-sms-app-id: 11111111
  qcloud-sms-app-key: 22222222

  aliyun-sms-enabled: false #阿里云短信
  aliyun-sms-region: cn-chengdu
  aliyun-sms-access-key: 333333333
  aliyun-sms-access-secret: 444444444

  yunpian-sms-enabled: false #云片短信
  yunpian-sms-api-key: 555555555
  
  #其他配置
  max-sms-per-second: 10 #每秒钟发送短信条数上限
```

> 注意：
>
> - 由于短信发送服务属于按量计费且受运营商管制，因此强烈建议设置 msg.max-sms-per-second 参数为一个合理的值，避免因调用方的失误造成短时间内发送大量短信
> - 只能开启1种app消息推送服务(msg.jpush-enabled / msg.getui-enabled 其中一项配置为 true)
> - 只能开启1种短信服务(msg.qcloud-sms-enabled / msg.aliyun-sms-enabled / msg.yunpian-sms-enabled 其中一项配置为 true)

#### 可能需要修改的参数介绍

| 参数名 | 参数用途 | 默认值 |
| ------ | --------- | -------- |
| msg.max-sms-per-second | 每秒钟发送短信条数上限 | 10 |
|  |  |  |
| msg.jpush-enabled | 是否启用极光推送 | false |
| msg.jpush-app-key | 极光推送的appKey | 无 |
| msg.jpush-master-secret | 极光推送的密钥 | 无 |
|  |  |  |
| msg.getui-enabled | 是否启用个推 | false |
| msg.getui-app-id | 个推的appId | 无 |
| msg.getui-url | 个推的url | 无 |
| msg.getui-master-secret | 个推的密钥 | 无 |
| msg.getui-app-key | 个推的appKey | 无 |
|  |  |  |
| msg.qcloud-sms-enabled | 是否启用腾讯云短信 | false |
| msg.qcloud-sms-app-id | 腾讯云短信appId | 无 |
| msg.qcloud-sms-app-key | 腾讯云短信appKey | 无 |
|  |  |  |
| msg.aliyun-sms-enabled | 是否启用阿里云短信 | false |
| msg.aliyun-sms-region | 阿里云短信区域编码 | cn-hangzhou |
| msg.aliyun-sms-access-key | 阿里云短信accessKey | 无 |
| msg.aliyun-sms-access-secret | 阿里云短信accessSecret | 无 |
|  |  |  |
| msg.yunpian-sms-enabled | 是否启用云片短信 | false |
| msg.yunpian-sms-api-key | 云片apiKey | 无 |
|  |  |  |
| spring.mail.host | 邮件SMTP服务器IP地址或主机名 | 无 |
| spring.mail.port | 邮件SMTP服务器端口号 | 无 |
| spring.mail.username | 邮件SMTP服务器用户名 | 无 |
| spring.mail.password | 邮件SMTP服务器密码 | 无 |
| spring.mail.default-encoding | 邮件默认编码 | UTF-8 |
|  |  |  |
| spring.kafka.bootstrap-servers | Apache Kafka 主机IP地址及端口号 | 无 |



-----



### 消息格式说明

#### APP消息格式
```json
{
  "messageId": "1234567891",
  "messageType": "app",
  "receiverList": ["asdfghjkl", "poiuytre", "zxcvbnm"],
  "title": "通知标题",
  "content": "内容",
  "extras": {
    "userId": 9527,
    "orderId":"9875455214575"
  }
}
```
> - messageId为全局唯一的消息标识
> - messageType固定为app
> - receiver为设备在推送平台的别名（需要app端遵照服务商的约定进行绑定）
> - extras为额外的业务参数（需与app端约定格式和参数值）
>



#### 短信格式

```json
{
  "messageId": "1234567892",
  "messageType": "sms",
  "receiver": "手机号",
  "templateId": "10001",
  "signName": "中国移不动",
  "templateParams": {
    "clientName": "张无忌",
    "value": "100"
  }
}
```
> - messageId为全局唯一的消息标识
> - messageType固定为sms
> - receiver为接收者手机号码
> - templateId为短信模板编号
> - signName为短信署名（上文已介绍）
> - templateParams为额外的业务参数（一般为短信推送服务商平台上定义的短信内容变量）



#### 邮件格式

```json
{
  "messageId":"1234567893",
  "messageType":"email",
  "receiver":"john@example.com",
  "subject":"这里是邮件标题",
  "htmlContent":true,
  "content":"这里是邮件正文",
  "attachmentList": ["/app/files/资料1.pdf", "/app/files/资料2.doc"]
}
```
> - messageId为全局唯一的消息标识
> - messageType固定为email
> - receiver为接收者邮箱地址
> - subject: 邮件标题
> - htmlContent: 邮件正文是否为HTML格式
> - content: 邮件正文
> - attachmentList: 邮件附件的完整路径，允许有多个
