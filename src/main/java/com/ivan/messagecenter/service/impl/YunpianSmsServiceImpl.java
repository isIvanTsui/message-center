package com.ivan.messagecenter.service.impl;

import cn.hutool.core.util.NumberUtil;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * 云片短信服务
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 14:56:35
 */
@Service
@Slf4j
@ConditionalOnProperty(name = SwitchNames.YUNPIAN, havingValue = "true")
public class YunpianSmsServiceImpl implements SmsService {

    private static final String BASE_URL = "https://sms.yunpian.com/v2/sms/single_send.json";

    private static Map<String, String> headersMap = new HashMap<>();

    static {
        headersMap.put("Accept", "application/json;charset=utf-8");
        headersMap.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    }

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
        Map<String, String> paramsMap = new HashMap<>(16);
        paramsMap.put("apikey", messageProperties.getYunpianSmsApiKey());

        //电话号码
        paramsMap.put("mobile", message.getReceiver());

        //内容
        paramsMap.put("text", message.getContent());
        log.info("调用云片短信接口URL: {}, 请求参数: {}", BASE_URL, JSONUtil.toJsonStr(paramsMap));
        try {
            HttpResponse resp = HttpUtils.postForm(BASE_URL, headersMap, paramsMap);
            if (resp == null) {
                log.error("云片短信接口返回值为空");
                return MessageConstants.RESULT_ERROR;
            }
            if (NumberUtil.equals(resp.getCode(), HttpStatus.HTTP_OK)) {
                log.error("云片短信接口返回的 HTTP 状态码为{}", resp.getCode());
                return MessageConstants.RESULT_ERROR;
            }
            log.info("云片短信接口返回参数: {}", resp.getMessage());
            Map returnMap = JSONUtil.toBean(resp.getMessage(), Map.class);
            if (!"0".equals(Objects.toString(returnMap.get("code")))) {
                log.error("云片短信发送失败");
                return Objects.toString(returnMap.get("msg"), MessageConstants.RESULT_ERROR);
            }
            log.info("云片短信发送成功！");
            return MessageConstants.RESULT_OK;
        } catch (IOException e) {
            log.error("调用云片短信接口异常", e);
            return MessageConstants.RESULT_ERROR;
        }
    }
}
