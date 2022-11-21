package com.ivan.messagecenter.pusher;

import com.ivan.messagecenter.config.property.MessageProperties;
import com.ivan.messagecenter.model.AppMessage;
import com.ivan.messagecenter.model.BaseResult;
import com.ivan.messagecenter.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


/**
 * APP消息推送器
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 15:21:46
 */
@Component
@Slf4j
public class AppMessagePusher {

    @Autowired
    private MessageProperties messageProperties;

    @Autowired(required = false)
    private AppService appService;

    /**
     * 异步推送
     */
    @Async("appThreadPool")
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 2))
    public void push(AppMessage message) {
        if (message == null) {
            return;
        }
        if (CollectionUtils.isEmpty(message.getReceiverList())) {
            log.error("app消息的接收者为空");
            return;
        }
        if (messageProperties.getJpushEnabled()) {
            BaseResult result = appService.push(message);
            if (result != null && result.isSuccess()) {
                log.info("极光推送成功！messageId={}", message.getMessageId());
//                CallbackUtils.executeCallback(message, true, null);
            } else {
                throw new RuntimeException("极光推送异常");
            }
        } else {
            throw new RuntimeException("必须且只能开启1种app消息推送服务！");
        }
    }

    /**
     * 重试失败时的日志记录
     *
     * @param re
     */
    @Recover
    private void recover(Exception re, AppMessage message) {
        log.error("重试异常", re);
//        CallbackUtils.executeCallback(message, false, re.getMessage());
    }
}
