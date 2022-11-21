package com.ivan.messagecenter.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 消息配置参数
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 11:06:06
 */
@ConfigurationProperties(prefix = "msg")
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
     * 是否启用邮箱推送
     */
    private Boolean emailEnabled = false;

    /**
     * 是否启用极光推送
     */
    private Boolean jpushEnabled = false;

    /**
     * 极光推送的appKey
     */
    private String jpushAppKey;

    /**
     * 极光推送的密钥
     */
    private String jpushMasterSecret;

    /**
     * 是否启用个推
     */
    private Boolean getuiEnabled = false;

    /**
     * 个推的appId
     */
    private String getuiAppId;

    /**
     * 个推的url
     */
    private String getuiUrl;

    /**
     * 个推的密钥
     */
    private String getuiMasterSecret;

    /**
     * 个推的appKey
     */
    private String getuiAppKey;

    /**
     * 是否启用腾讯云短信
     */
    private Boolean qcloudSmsEnabled = false;

    /**
     * 腾讯云短信appId
     */
    private String qcloudSmsAppId;

    /**
     * 腾讯云短信appKey
     */
    private String qcloudSmsAppKey;

    /**
     * 是否启用阿里云短信
     */
    private Boolean aliyunSmsEnabled;

    /**
     * 阿里云短信区域
     */
    private String aliyunSmsRegion = "cn-chengdu";

    /**
     * 阿里云短信accessKey
     */
    private String aliyunSmsAccessKey;

    /**
     * 阿里云短信accessSecret
     */
    private String aliyunSmsAccessSecret;

    /**
     * 是否启用云片短信
     */
    private Boolean yunpianSmsEnabled;

    /**
     * 云片apiKey
     */
    private String yunpianSmsApiKey;
}
