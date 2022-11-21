package com.ivan.messagecenter.model;


import lombok.Data;

import java.io.Serializable;

/**
 * 消息体公共属性类
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 10:50:15
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -6218636958338729687L;
    /**
     * 全局唯一ID
     */
    private String messageId;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 回调URL
     */
    private String callbackUrl;
}
