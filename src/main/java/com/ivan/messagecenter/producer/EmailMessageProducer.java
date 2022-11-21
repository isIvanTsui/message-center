package com.ivan.messagecenter.producer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.eventbus.EventBus;
import com.ivan.messagecenter.config.property.MessageProperties;
import com.ivan.messagecenter.constant.MessageConstants;
import com.ivan.messagecenter.constant.MqTypeNames;
import com.ivan.messagecenter.consumer.EmailMessageConsumer;
import com.ivan.messagecenter.model.EmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 向消息队列中发送邮件消息
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 10:40:23
 */
@Service
public class EmailMessageProducer {
    @Autowired(required = false)
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MessageProperties messageProperties;

    @Autowired
    private EmailMessageConsumer emailMessageConsumer;

    private final EventBus eventBus = new EventBus();

    /**
     * 将邮件发送到mq
     *
     * @param emailMessage 电子邮件消息
     * @return {@code String}
     */
    public String send(EmailMessage emailMessage) {
        if (emailMessage != null && !"".equals(emailMessage.getReceiver()) && !"".equals(emailMessage.getContent())) {
            if (StrUtil.equalsIgnoreCase(messageProperties.getMqType(), MqTypeNames.KAFKA)) {
                kafkaTemplate.send(MessageConstants.KAFKA_EMAIL_MSG_TOPIC, JSONUtil.toJsonStr(emailMessage));
            } else if (StrUtil.equalsIgnoreCase(messageProperties.getMqType(), MqTypeNames.EVENT_BUS)) {
                eventBus.register(emailMessageConsumer);
                eventBus.post(emailMessage);
            }
            return "发送成功";
        }
        return "发送失败";
    }
}
