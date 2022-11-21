package com.ivan.messagecenter.consumer;

import cn.hutool.json.JSONUtil;
import com.google.common.eventbus.Subscribe;
import com.ivan.messagecenter.constant.MessageConstants;
import com.ivan.messagecenter.model.SmsMessage;
import com.ivan.messagecenter.pusher.SmsMessagePusher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * 监听消息队列中的短信消息并进行处理
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 11:01:59
 */
@Component
@Slf4j
public class SmsMessageConsumer {

    @Autowired
    private SmsMessagePusher smsMessagePusher;

    /**
     * 接收短信队列的消息
     *
     * @param payload
     */
    @KafkaListener(id = "smsMessageConsumer", groupId = MessageConstants.KAFKA_GROUP_ID, topics = MessageConstants.KAFKA_SMS_MSG_TOPIC)
    public void receive(String payload) {
        log.info("接收到短信消息: {}", payload);
        try {
            SmsMessage smsMessage = JSONUtil.toBean(payload, SmsMessage.class);
            smsMessagePusher.push(smsMessage);
        } catch (Exception e) {
            log.error("发送短信时出现异常", e);
        }
    }

    /**
     * eventBus接收端
     *
     * @param smsMessage sms消息
     */
    @Subscribe
    public void receive(SmsMessage smsMessage) {
        log.info("接收到短信消息: {}", JSONUtil.parse(smsMessage));
        try {
            smsMessagePusher.push(smsMessage);
        } catch (Exception e) {
            log.error("发送短信时出现异常", e);
        }
    }
}
