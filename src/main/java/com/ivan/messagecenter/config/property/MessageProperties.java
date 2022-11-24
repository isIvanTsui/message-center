package com.ivan.messagecenter.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 测试属性
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/24 15:50:48
 */
@Component
@ConfigurationProperties("msg")
@Data
public class MessageProperties {
    /**
     * mq类型
     */
    private String mqType;

    /**
     * 每秒钟最多发送几条短信
     */
    private Integer maxSmsPerSecond;

    /**
     * 个推
     */
    private AppProperties getui;

    /**
     * 极光推送
     */
    private AppProperties jpush;

    /**
     * 阿里云短信
     */
    private SmsProperties aliyun;

    /**
     * 腾讯云短信
     */
    private SmsProperties qcloud;

    /**
     * 云片短信
     */
    private SmsProperties yunpian;
}
