package com.ivan.messagecenter.controller;

import com.ivan.messagecenter.model.EmailMessage;
import com.ivan.messagecenter.producer.EmailMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息测试
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 12:02:51
 */
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
