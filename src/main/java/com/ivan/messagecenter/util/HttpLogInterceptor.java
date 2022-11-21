package com.ivan.messagecenter.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


/**
 * HTTP请求日志拦截器
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 14:45:21
 */
@Slf4j
public class HttpLogInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        long start = System.currentTimeMillis();
        Request request = chain.request();
        log.debug("发起请求到: {}, 请求头信息: {}", request.url(), request.headers());
        Response response = chain.proceed(request);
        long end = System.currentTimeMillis();
        log.debug("收到来自 {} 的响应, 耗时 {} 毫秒", request.url(), end - start);
        return response;
    }
}
