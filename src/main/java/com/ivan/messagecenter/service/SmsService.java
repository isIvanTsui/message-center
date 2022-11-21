package com.ivan.messagecenter.service;

import com.ivan.messagecenter.model.SmsMessage;

/**
 * 短信服务
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 13:58:52
 */
public interface SmsService {
    /**
     * 发送短信
     *
     * @param smsMessage sms消息
     * @return {@code String}
     */
    String send(SmsMessage smsMessage);
}
