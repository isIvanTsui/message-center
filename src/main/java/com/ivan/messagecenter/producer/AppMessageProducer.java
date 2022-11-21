package com.ivan.messagecenter.producer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.eventbus.EventBus;
import com.ivan.messagecenter.config.property.MessageProperties;
import com.ivan.messagecenter.constant.MessageConstants;
import com.ivan.messagecenter.constant.MqTypeNames;
import com.ivan.messagecenter.consumer.AppMessageConsumer;
import com.ivan.messagecenter.model.SmsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 向消息队列中发送APP消息
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 15:08:45
 */
@Service
public class AppMessageProducer {
    @Autowired(required = false)
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MessageProperties messageProperties;

    @Autowired
    private AppMessageConsumer appMessageConsumer;

    private final EventBus eventBus = new EventBus();

    /**
     * 将APP消息发送到MQ
     *
     * @param smsMessage sms消息
     * @return {@code String}
     */
    public String send(SmsMessage smsMessage) {
        if (smsMessage != null && !"".equals(smsMessage.getReceiver()) && !"".equals(smsMessage.getContent())) {
            if (StrUtil.equalsIgnoreCase(messageProperties.getMqType(), MqTypeNames.KAFKA)) {
                kafkaTemplate.send(MessageConstants.KAFKA_APP_MSG_TOPIC, JSONUtil.toJsonStr(smsMessage));
            } else if (StrUtil.equalsIgnoreCase(messageProperties.getMqType(), MqTypeNames.EVENT_BUS)) {
                eventBus.register(appMessageConsumer);
                eventBus.post(smsMessage);
            }
            return "发送成功";
        }
        return "发送失败";
    }
}
