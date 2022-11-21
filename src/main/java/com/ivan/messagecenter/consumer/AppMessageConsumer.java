package com.ivan.messagecenter.consumer;

import cn.hutool.json.JSONUtil;
import com.google.common.eventbus.Subscribe;
import com.ivan.messagecenter.constant.MessageConstants;
import com.ivan.messagecenter.model.AppMessage;
import com.ivan.messagecenter.pusher.AppMessagePusher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 监听消息队列中APP消息并进行处理
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/21 15:11:16
 */
@Component
@Slf4j
public class AppMessageConsumer {
    @Autowired
    private AppMessagePusher appMessagePusher;

    /**
     * 接收邮件队列的消息
     *
     * @param payload
     */
    @KafkaListener(id = "appMessageConsumer", groupId = MessageConstants.KAFKA_GROUP_ID, topics = MessageConstants.KAFKA_APP_MSG_TOPIC)
    public void receive(String payload) {
        log.info("接收到应用消息: {}", payload);
        try {
            AppMessage appMessage = JSONUtil.toBean(payload, AppMessage.class);
            appMessagePusher.push(appMessage);
        } catch (Exception e) {
            log.error("发送邮件时出现异常", e);
        }
    }

    /**
     * eventBus接收端
     *
     * @param appMessage 应用程序消息
     */
    @Subscribe
    public void receive(AppMessage appMessage) {
        log.info("接收到应用消息: {}", JSONUtil.parse(appMessage));
        try {
            appMessagePusher.push(appMessage);
        } catch (Exception e) {
            log.error("发送邮件时出现异常", e);
        }
    }
}
