package com.ivan.messagecenter.util;


import lombok.Data;

import java.util.Map;


/**
 * http响应
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 14:37:45
 */
@Data
public class HttpResponse {
    private static final long serialVersionUID = -5475690197719799772L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 响应文本
     */
    private String message;

    /**
     * 响应headers
     */
    private Map<String, String> headers;
}
