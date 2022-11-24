package com.ivan.messagecenter.config.property;

/**
 * 开关名字
 *
 * @author yingfan.cui@dezhentech.com
 * @date 2022/11/24 14:26:18
 */
public interface SwitchNames {
    /**
     * 邮件开关属性
     */
    String EMAIL = "msg.email-enabled";

    /**
     * App开关属性
     */
    String JPUSH = "msg.jpush-enabled";
    String GETUI = "msg.getui-enabled";

    /**
     * 短信开关属性
     */
    String QCLOUD = "msg.qcloud-sms-enabled";
    String ALIYUN = "msg.aliyun-sms-enabled";
    String YUNPIAN = "msg.yunpian-sms-enabled";
}
