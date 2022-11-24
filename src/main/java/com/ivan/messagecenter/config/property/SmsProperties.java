package com.ivan.messagecenter.config.property;

import lombok.Data;

/**
 * 短信推送属性
 *
 * @author yingfan.cui@dezhentech.com
 * @date 2022/11/24 15:41:23
 */
@Data
public class SmsProperties {
    /**
     * 是否启用短信推送
     */
    private Boolean enabled = false;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * accessSecret
     */
    private String accessSecret;

    /**
     * appId
     */
    private String appId;

    /**
     * appKey
     */
    private String appKey;

    /**
     * api密匙
     */
    private String apiKey;

    /**
     * 地区
     */
    private String region = "cn-chengdu";
}
