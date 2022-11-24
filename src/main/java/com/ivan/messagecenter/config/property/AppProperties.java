package com.ivan.messagecenter.config.property;

import lombok.Data;

/**
 * APP推送属性
 *
 * @author yingfan.cui@dezhentech.com
 * @date 2022/11/24 15:38:07
 */
@Data
public class AppProperties {
    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * appId
     */
    private String appId;

    /**
     * url
     */
    private String url;

    /**
     * appKey
     */
    private String appKey;

    /**
     * 密钥
     */
    private String masterSecret;


}
