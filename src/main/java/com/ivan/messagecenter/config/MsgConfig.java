package com.ivan.messagecenter.config;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import com.ivan.messagecenter.config.property.MessageProperties;
import com.ivan.messagecenter.config.property.SwitchNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 总配置文件
 *
 * @author is.ivan.tsui@gmail.com
 * @date 2022/11/21 15:14:58
 */
@Configuration
@EnableAsync
@Slf4j
@EnableConfigurationProperties(MessageProperties.class)
public class MsgConfig {
    @Autowired
    private MessageProperties messageProperties;

    //线程池默认核心线程数
    private static final int POOL_CORE_SIZE = 4;

    /**
     * 初始化线程池
     */
    @PostConstruct
    private void initBean() {
        //判断APP推送配置
        int appEnableds = BooleanUtil.toInt(BooleanUtil.isTrue(messageProperties.getJpushEnabled()))
                + BooleanUtil.toInt(BooleanUtil.isTrue(messageProperties.getGetuiEnabled()));
        if (appEnableds > 1) {
            throw new RuntimeException("只能开启1种APP推送服务");
        }
        //判断短信推送配置
        int smsEnableds = BooleanUtil.toInt(BooleanUtil.isTrue(messageProperties.getAliyunSmsEnabled()))
                + BooleanUtil.toInt(BooleanUtil.isTrue(messageProperties.getYunpianSmsEnabled()))
                + BooleanUtil.toInt(BooleanUtil.isTrue(messageProperties.getQcloudSmsEnabled()));
        if (smsEnableds > 1) {
            throw new RuntimeException("只能开启1种短信推送服务");
        }
    }

    /**
     * 配置用于app消息推送的线程池
     *
     * @return
     */
    @Bean("appThreadPool")
    @Conditional(AppCondition.class)
    public AsyncTaskExecutor appMsgThreadPool() {
        int appEnableds = BooleanUtil.toInt(BooleanUtil.isTrue(messageProperties.getJpushEnabled()))
                + BooleanUtil.toInt(BooleanUtil.isTrue(messageProperties.getGetuiEnabled()));
        if (NumberUtil.equals(appEnableds, 1)) {
            return createThreadPoolTaskExecutor("appmsg-thread-");
        }
        return null;
    }

    /**
     * 配置用于短信发送的线程池
     *
     * @return
     */
    @Bean("smsThreadPool")
    @Conditional(SmsCondition.class)
    public AsyncTaskExecutor smsThreadPool() {
        int smsEnableds = BooleanUtil.toInt(BooleanUtil.isTrue(messageProperties.getAliyunSmsEnabled()))
                + BooleanUtil.toInt(BooleanUtil.isTrue(messageProperties.getYunpianSmsEnabled()))
                + BooleanUtil.toInt(BooleanUtil.isTrue(messageProperties.getQcloudSmsEnabled()));
        if (NumberUtil.equals(smsEnableds, 1)) {
            return createThreadPoolTaskExecutor("sms-thread-");
        }
        return null;
    }

    /**
     * 配置用于邮件发送的线程池
     *
     * @return
     */
    @Bean("emailThreadPool")
    @ConditionalOnProperty(name = SwitchNames.EMAIL, havingValue = "true")
    public AsyncTaskExecutor emailThreadPool() {
        return createThreadPoolTaskExecutor("email-thread-");
    }

    /**
     * 创建线程池
     *
     * @param prefix 线程名前缀
     * @return 线程池实例
     */
    private ThreadPoolTaskExecutor createThreadPoolTaskExecutor(String prefix) {
        int coreSize = POOL_CORE_SIZE;
        int maxSize = Runtime.getRuntime().availableProcessors();
        if (coreSize > maxSize) {
            coreSize = maxSize;
        }
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(coreSize * 2048);
        executor.setKeepAliveSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix(prefix);
        log.info("线程池大小为: {}, 前缀: {}", coreSize, prefix);
        return executor;
    }
}
