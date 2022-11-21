package com.ivan.messagecenter.consumer;

import cn.hutool.json.JSONUtil;
import com.google.common.eventbus.Subscribe;
import com.ivan.messagecenter.constant.MessageConstants;
import com.ivan.messagecenter.model.EmailMessage;
import com.ivan.messagecenter.pusher.EmailMessagePusher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * 监听消息队列中邮件消息并进行处理
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 11:01:59
 */
@Component
@Slf4j
public class EmailMessageConsumer {

    @Autowired
    private EmailMessagePusher emailMessagePusher;

    /**
     * 接收邮件队列的消息
     *
     * @param payload
     */
    @KafkaListener(id = "emailMessageConsumer", groupId = MessageConstants.KAFKA_GROUP_ID, topics = MessageConstants.KAFKA_EMAIL_MSG_TOPIC)
    public void receive(String payload) {
        log.info("接收到邮件消息: {}", payload);
        try {
            EmailMessage emailMessage = JSONUtil.toBean(payload, EmailMessage.class);
            emailMessagePusher.push(emailMessage);
        } catch (Exception e) {
            log.error("发送邮件时出现异常", e);
        }
    }

    /**
     * eventBus接收端
     *
     * @param emailMessage 电子邮件消息
     */
    @Subscribe
    public void receive(EmailMessage emailMessage) {
        log.info("接收到邮件消息: {}", JSONUtil.parse(emailMessage));
        try {
            emailMessagePusher.push(emailMessage);
        } catch (Exception e) {
            log.error("发送邮件时出现异常", e);
        }
    }
}
