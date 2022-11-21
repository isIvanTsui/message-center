package com.ivan.messagecenter.util;

import lombok.Data;
import okhttp3.Authenticator;

import java.net.Proxy;


/**
 * http代理配置
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 14:42:29
 */
@Data
public class HttpProxyConfig {

    /**
     * 主机或IP
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 是否需要验证
     */
    private boolean needAuth;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 代理
     */
    private Proxy proxy;

    /**
     * 验证器
     */
    private Authenticator authenticator;
}
