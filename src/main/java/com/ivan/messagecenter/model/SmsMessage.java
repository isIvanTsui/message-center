package com.ivan.messagecenter.model;

import lombok.Data;

import java.util.Map;


/**
 * 短信消息
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 13:42:44
 */
@Data
public class SmsMessage extends Message {
    private static final long serialVersionUID = -4732866581871201935L;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板ID
     */
    private String templateId;

    /**
     * 短信模板参数
     */
    private Map<String, String> templateParams;

    /**
     * 短信内容（仅用于支持直接传入内容的短信服务商）
     */
    private String content;
}
