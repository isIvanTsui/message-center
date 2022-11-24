package com.ivan.messagecenter.pusher;

import cn.hutool.core.util.BooleanUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.ivan.messagecenter.config.property.MessageProperties;
import com.ivan.messagecenter.constant.MessageConstants;
import com.ivan.messagecenter.model.SmsMessage;
import com.ivan.messagecenter.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;


/**
 * 短信消息推送式
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 13:47:53
 */
@Service
@Slf4j
public class SmsMessagePusher {

    @Autowired
    private MessageProperties messageProperties;

    @Autowired(required = false)
    private SmsService smsService;

    //限流器
    private RateLimiter rateLimiter;

    /**
     * 异步推送
     */
    @Async("smsThreadPool")
    public void push(SmsMessage message) {
        if (rateLimiter.tryAcquire(1, MessageConstants.SMS_WAIT_SECONDS, TimeUnit.SECONDS)) {
            String code = sendSms(message);
            if (MessageConstants.RESULT_OK.equalsIgnoreCase(code)) {
                log.info("已成功发送短信到手机号 {}, messageId={}", message.getReceiver(), message.getMessageId());
            } else {
                log.error("无法发送短信到手机号 {}, messageId={}", message.getReceiver(), message.getMessageId());
            }
        } else {
            throw new RuntimeException("已达到短信限流策略的上限");
        }
    }

    /**
     * 发送短信
     *
     * @param message 短信
     * @return 返回码
     */
    private String sendSms(SmsMessage message) {
        if (message == null) {
            return null;
        }
        if (BooleanUtil.isTrue(messageProperties.getAliyun().getEnabled())
                || BooleanUtil.isTrue(messageProperties.getQcloud().getEnabled())
                || BooleanUtil.isTrue(messageProperties.getYunpian().getEnabled())) {
            return smsService.send(message);
        } else {
            throw new RuntimeException("未开启短信服务！");
        }
    }

    /**
     * 检查参数
     */
    @PostConstruct
    private void checkSmsServices() {
        int maxSmsPerSecond = MessageConstants.DEFAULT_SMS_PER_SECOND;
        if (messageProperties.getMaxSmsPerSecond() != null && messageProperties.getMaxSmsPerSecond() > 0) {
            maxSmsPerSecond = messageProperties.getMaxSmsPerSecond();
        }
        rateLimiter = RateLimiter.create(maxSmsPerSecond);
        log.info("短信限流策略的上限为每秒 {} 条", maxSmsPerSecond);
    }
}
