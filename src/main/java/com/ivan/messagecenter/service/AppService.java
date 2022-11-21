package com.ivan.messagecenter.service;

import com.ivan.messagecenter.model.AppMessage;
import com.ivan.messagecenter.model.BaseResult;

/**
 * App服务
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 15:44:33
 */
public interface AppService {
    /**
     * 推送app消息
     *
     * @param appMessage
     * @return
     */
    BaseResult push(AppMessage appMessage);
}
