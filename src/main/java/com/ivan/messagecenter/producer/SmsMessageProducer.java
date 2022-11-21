package com.ivan.messagecenter.producer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.eventbus.EventBus;
import com.ivan.messagecenter.config.property.MessageProperties;
import com.ivan.messagecenter.constant.MessageConstants;
import com.ivan.messagecenter.constant.MqTypeNames;
import com.ivan.messagecenter.consumer.SmsMessageConsumer;
import com.ivan.messagecenter.model.SmsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 向消息队列中发送短信消息
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 15:09:38
 */
@Service
public class SmsMessageProducer {
    @Autowired(required = false)
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MessageProperties messageProperties;

    @Autowired
    private SmsMessageConsumer smsMessageConsumer;

    private final EventBus eventBus = new EventBus();

    /**
     * 将短信发送到mq
     *
     * @param smsMessage sms消息
     * @return {@code String}
     */
    public String send(SmsMessage smsMessage) {
        if (smsMessage != null && !"".equals(smsMessage.getReceiver()) && !"".equals(smsMessage.getContent())) {
            if (StrUtil.equalsIgnoreCase(messageProperties.getMqType(), MqTypeNames.KAFKA)) {
                kafkaTemplate.send(MessageConstants.KAFKA_SMS_MSG_TOPIC, JSONUtil.toJsonStr(smsMessage));
            } else if (StrUtil.equalsIgnoreCase(messageProperties.getMqType(), MqTypeNames.EVENT_BUS)) {
                eventBus.register(smsMessageConsumer);
                eventBus.post(smsMessage);
            }
            return "发送成功";
        }
        return "发送失败";
    }
}
