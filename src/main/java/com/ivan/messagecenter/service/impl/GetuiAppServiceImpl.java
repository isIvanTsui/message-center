package com.ivan.messagecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.gexin.rp.sdk.http.IGtPush;
import com.ivan.messagecenter.config.property.MessageProperties;
import com.ivan.messagecenter.model.AppMessage;
import com.ivan.messagecenter.model.BaseResult;
import com.ivan.messagecenter.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 个推推送实现
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 15:46:01
 */
@Service
@Slf4j
@ConditionalOnProperty(prefix = "msg", name = "getuiEnabled", havingValue = "getuiEnabled")
public class GetuiAppServiceImpl implements AppService {

    @Autowired
    private MessageProperties messageProperties;

    /**
     * 缓存
     */
    private static Map<String, IGtPush> iGtPushMap = new ConcurrentHashMap<>();

    /**
     * 推送app消息
     *
     * @param appMessage
     * @return
     */
    @Override
    public BaseResult push(AppMessage appMessage) {
        if (appMessage == null) {
            throw new RuntimeException("个推消息为空");
        }
        if (CollectionUtil.isEmpty(appMessage.getReceiverList())) {
            throw new RuntimeException("个推消息接收者为空");
        }
        log.info("个推消息开始推送: {}", appMessage.getContent());
        //TODO  个推 找不到类，这里还未完成
        /*TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(messageProperties.getGetuiAppId());
        template.setAppkey(messageProperties.getGetuiAppKey());
        template.setTransmissionType(1);
        template.setTransmissionContent(appMessage.getContent());
        List<TagTarget> tagTargetList = new ArrayList<>(appMessage.getReceiverList().size());
        for (String receiver : appMessage.getReceiverList()) {
            TagTarget tagTarget = new TagTarget();
            tagTarget.setAppId(messageProperties.getGetuiAppId());
            tagTarget.setClientId(receiver);
            tagTargetList.add(tagTarget);
        }

        IPushResult ret = getGtClient().pushMessageToListByTag(template.getTransmissionContent(), tagTargetList);
        if (ret == null) {
            throw new RuntimeException("个推响应为空");
        }
        log.info("个推响应数据: {}", ret);*/
        BaseResult result = new BaseResult();
        result.setMessageId(appMessage.getMessageId());
        result.setSuccess(true);
        return result;
    }

    /**
     * 获取客户端实例
     */
    private IGtPush getGtClient() {
        String key = messageProperties.getGetuiAppId();
        return iGtPushMap.computeIfAbsent(key, k -> new IGtPush(messageProperties.getGetuiUrl(), messageProperties.getGetuiAppKey(), messageProperties.getGetuiMasterSecret()));
    }
}
