package com.ivan.messagecenter.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.ivan.messagecenter.config.property.MessageProperties;
import com.ivan.messagecenter.config.property.SwitchNames;
import com.ivan.messagecenter.constant.MessageConstants;
import com.ivan.messagecenter.model.SmsMessage;
import com.ivan.messagecenter.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;


/**
 * 阿里云短信服务
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 14:02:15
 */
@Service
@Slf4j
@ConditionalOnProperty(name = SwitchNames.ALIYUN, havingValue = "true")
public class AliyunSmsServiceImpl implements SmsService {

    private IAcsClient client;

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
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", message.getReceiver());
        request.putQueryParameter("SignName", message.getSignName());
        request.putQueryParameter("TemplateCode", message.getTemplateId());
        request.putQueryParameter("TemplateParam", JSONUtil.toJsonStr(message.getTemplateParams()));
        try {
            CommonResponse sendSmsResponse = client.getCommonResponse(request);
            if (sendSmsResponse == null) {
                log.error("阿里云短信网关返回数据为空");
                return MessageConstants.RESULT_ERROR;
            }
            log.info("阿里云短信网关返回数据: {}", JSONUtil.toJsonStr(sendSmsResponse));
            if (sendSmsResponse.getHttpStatus() == HttpStatus.HTTP_OK) {
                String dataStr = sendSmsResponse.getData();
                Map dataMap = JSONUtil.toBean(dataStr, Map.class);
                if (MapUtil.isNotEmpty(dataMap)) {
                    String code = Objects.toString(dataMap.get("Code"), "");
                    if ("OK".equals(code)) {
                        log.info("阿里云短信发送成功!");
                        return MessageConstants.RESULT_OK;
                    }
                }
            }
        } catch (ClientException e) {
            log.error("阿里云短信发送异常", e);
        }
        return MessageConstants.RESULT_ERROR;
    }

    /**
     * 初始化
     */
    @PostConstruct
    private void initClient() {
        DefaultProfile profile = DefaultProfile.getProfile(messageProperties.getAliyunSmsRegion(), messageProperties.getAliyunSmsAccessKey(), messageProperties.getAliyunSmsAccessSecret());
        client = new DefaultAcsClient(profile);
    }
}
