package com.ivan.messagecenter.model;


import lombok.Data;


/**
 * 结果
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 15:43:37
 */
@Data
public class BaseResult {
    private static final long serialVersionUID = -1036893187412443132L;

    /**
     * 消息唯一ID
     */
    private String messageId;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 原始响应数据
     */
    private String response;

    /**
     * 失败原因
     */
    private String errorMsg;
}
