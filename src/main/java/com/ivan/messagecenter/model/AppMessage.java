package com.ivan.messagecenter.model;

import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * APP消息
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 15:17:58
 */
@Data
public class AppMessage extends Message {
    private static final long serialVersionUID = 7539543631361705983L;

    /**
     * 接收者列表
     */
    private List<String> receiverList;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 附加业务参数
     */
    private Map<String, String> extras;

}
