package com.ivan.messagecenter.constant;


/**
 * 消息常量
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/18 10:49:51
 */
public class MessageConstants {

    /**
     * OK
     */
    public static final String RESULT_OK = "OK";

    /**
     * ERROR
     */
    public static final String RESULT_ERROR = "ERROR";

    /**
     * 用于kafka消费者端的分组ID
     */
    public static final String KAFKA_GROUP_ID = "message-center";

    /**
     * app消息
     */
    public static final String APP = "app";

    /**
     * 短信
     */
    public static final String SMS = "sms";

    /**
     * 电子邮件
     */
    public static final String EMAIL = "email";

    /**
     * app消息对应的kafka topic名称
     */
    public static final String KAFKA_APP_MSG_TOPIC = "app-msg-topic";

    /**
     * 短信对应的kafka topic名称
     */
    public static final String KAFKA_SMS_MSG_TOPIC = "sms-msg-topic";

    /**
     * 电子邮件对应的kafka topic名称
     */
    public static final String KAFKA_EMAIL_MSG_TOPIC = "email-msg-topic";

    /**
     * 每秒短信条数(默认值)
     */
    public static final int DEFAULT_SMS_PER_SECOND = 10;

    /**
     * 短信限流时的等待时间(秒)
     */
    public static final int SMS_WAIT_SECONDS = 10;
}
