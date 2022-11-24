package com.ivan.messagecenter.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.ivan.messagecenter.config.property.MessageProperties;
import com.ivan.messagecenter.config.property.SwitchNames;
import com.ivan.messagecenter.constant.MessageConstants;
import com.ivan.messagecenter.model.SmsMessage;
import com.ivan.messagecenter.service.SmsService;
import com.ivan.messagecenter.util.HttpResponse;
import com.ivan.messagecenter.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * 腾讯云短信服务
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 16:22:53
 */
@Service
@Slf4j
@ConditionalOnProperty(name = SwitchNames.QCLOUD, havingValue = "true")
public class QCloudSmsServiceImpl implements SmsService {

    private static final String BASE_URL = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid=%s&random=%s";

    @Resource
    private MessageProperties messageProperties;

    /**
     * 发送短信
     *
     * @param message 短信
     * @return 状态码
     */
    @Override
    public String send(SmsMessage message) {
        if (message == null) {
            return null;
        }
        String randomNumber = IdUtil.nanoId(10);
        Map<String, Object> paramsMap = new HashMap<>(16);

        //电话号码
        Map<String, Object> telMap = new HashMap<>(16);
        telMap.put("mobile", message.getReceiver());
        telMap.put("nationcode", "86");
        paramsMap.put("tel", telMap);

        //模板ID
        paramsMap.put("tpl_id", Long.parseLong(message.getTemplateId()));

        //短信签名内容
        paramsMap.put("sign", message.getSignName());

        //模板参数
        List<String> templateParams = new LinkedList<>();
        if (message.getTemplateParams() != null && !message.getTemplateParams().isEmpty()) {
            for (Map.Entry<String, String> entry : message.getTemplateParams().entrySet()) {
                templateParams.add(entry.getValue());
            }
        }
        paramsMap.put("params", templateParams);

        //时间戳
        long timestamp = System.currentTimeMillis();
        paramsMap.put("time", timestamp);

        //App 凭证
        String src = messageProperties.getQcloudSmsAppKey() + "&random=" + randomNumber + "&time=" + timestamp + "&mobile=" + message.getReceiver();
        paramsMap.put("sig", DigestUtil.sha256(src.getBytes(StandardCharsets.UTF_8)));

        String url = String.format(BASE_URL, messageProperties.getQcloudSmsAppId(), randomNumber);
        String requestJson = JSONUtil.toJsonStr(paramsMap);
        log.info("调用腾讯云短信接口URL: {}, 请求参数: {}", url, requestJson);
        try {
            HttpResponse resp = HttpUtils.postJson(url, new HashMap<>(16), requestJson);
            if (resp == null) {
                log.error("腾讯云短信接口返回值为空");
                return MessageConstants.RESULT_ERROR;
            }
            if (!NumberUtil.equals(resp.getCode(), HttpStatus.HTTP_OK)) {
                log.error("腾讯云短信接口返回的 HTTP 状态码为{}", resp.getCode());
                return MessageConstants.RESULT_ERROR;
            }
            log.info("腾讯云短信接口返回参数: {}", resp.getMessage());
            Map returnMap = JSONUtil.toBean(resp.getMessage(), Map.class);
            if (!"0".equals(Objects.toString(returnMap.get("result")))) {
                log.error("腾讯云短信发送失败");
                return Objects.toString(returnMap.get("errmsg"), MessageConstants.RESULT_ERROR);
            }
            log.info("腾讯云短信发送成功！");
            return MessageConstants.RESULT_OK;
        } catch (IOException e) {
            log.error("调用腾讯云短信接口异常", e);
            return MessageConstants.RESULT_ERROR;
        }
    }
}
