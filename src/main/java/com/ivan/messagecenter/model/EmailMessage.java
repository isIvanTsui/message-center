package com.ivan.messagecenter.model;

import lombok.Data;

import java.util.List;


/**
 * 电子邮件消息
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 11:16:34
 */
@Data
public class EmailMessage extends Message {
    private static final long serialVersionUID = 5388297236840437564L;
    /**
     * 接收者邮箱地址
     */
    private String receiver;

    /**
     * 标题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否为HTML内容
     */
    private boolean htmlContent;

    /**
     * 附件
     */
    private List<String> attachmentList;
}
