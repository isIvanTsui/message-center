package com.ivan.messagecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.ivan.messagecenter.config.property.MessageProperties;
import com.ivan.messagecenter.model.AppMessage;
import com.ivan.messagecenter.model.BaseResult;
import com.ivan.messagecenter.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 极光推送实现
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 16:09:42
 */
@Service
@Slf4j
@ConditionalOnProperty(prefix = "msg", name = "jpushEnabled", havingValue = "true")
public class JPushAppServiceImpl implements AppService {

    @Autowired
    private MessageProperties messageProperties;

    /**
     * 缓存
     */
    private static Map<String, JPushClient> jPushClientMap = new ConcurrentHashMap<>();

    /**
     * 推送app消息
     *
     * @param appMessage
     * @return
     */
    @Override
    public BaseResult push(AppMessage appMessage) {
        if (appMessage == null) {
            throw new RuntimeException("极光消息为空");
        }
        if (CollectionUtil.isEmpty(appMessage.getReceiverList())) {
            throw new RuntimeException("极光消息接收者为空");
        }
        log.info("极光消息开始推送: {}", appMessage.getContent());
        BaseResult result = new BaseResult();
        result.setMessageId(appMessage.getMessageId());
        result.setSuccess(false);
        List<String> aliasList = appMessage.getReceiverList();
        PushPayload payload = getPayloadByAlias(aliasList, appMessage.getTitle(), appMessage.getExtras());
        if (push(payload)) {
            log.info("极光消息推送成功");
            result.setSuccess(true);
            return result;
        }
        log.error("极光消息推送失败");
        result.setErrorMsg("失败");
        return result;
    }

    /**
     * 进行推送
     *
     * @param payload 消息体
     * @return
     */
    private boolean push(PushPayload payload) {
        log.info("极光推送消息体: {}", payload.toJSON());
        try {
            PushResult result = getJPushClient().sendPush(payload);
            if (result != null) {
                if (200 == result.getResponseCode()) {
                    log.info("极光消息推送成功，消息ID: {}", result.msg_id);
                    return true;
                } else {
                    log.error("极光消息推送失败，ResponseCode={}", result.getResponseCode());
                }
            }
        } catch (APIConnectionException e) {
            log.error("推送极光消息出现APIConnectionException异常", e);
        } catch (APIRequestException e) {
            log.error("推送极光消息出现APIRequestException异常", e);
            log.error("推送极光消息出现异常, status={}, errorCode={}, errorMessage={}", e.getStatus(), e.getErrorCode(), e.getErrorMessage());
        }
        return false;
    }

    /**
     * 生成文本消息对象
     *
     * @param aliasList 别名列表
     * @param alert     提醒文字
     * @param extras    自定义参数
     * @return
     */
    private PushPayload getPayloadByAlias(List<String> aliasList, String alert, Map<String, String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(aliasList))
                //IOS配置为生产环境才能收到推送，参考：https://community.jiguang.cn/t/ios/13452/7
                .setOptions(Options.newBuilder().setApnsProduction(true).build())
                .setNotification(
                        Notification
                                .newBuilder()
                                .setAlert(alert)
                                .addPlatformNotification(
                                        IosNotification.newBuilder().setSound("default").setBadge(1).addExtras(extras)
                                                .build())
                                .addPlatformNotification(AndroidNotification.newBuilder().setBuilderId(1).addExtras(extras).build()).build()).build();
    }

    /**
     * 获取客户端实例
     */
    private JPushClient getJPushClient() {
        String key = messageProperties.getJpushAppKey();
        return jPushClientMap.computeIfAbsent(key, k -> {
            ClientConfig clientConfig = ClientConfig.getInstance();
            return new JPushClient(messageProperties.getJpushMasterSecret(), messageProperties.getJpushAppKey(), null, clientConfig);
        });
    }
}
