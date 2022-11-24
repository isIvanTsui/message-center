package com.ivan.messagecenter.config;

import cn.hutool.core.util.StrUtil;
import com.ivan.messagecenter.config.property.SwitchNames;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 短信推送配置判断
 *
 * @author yingfan.cui@dezhentech.com
 * @date 2022/11/24 14:35:05
 */
public class SmsCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        boolean isOpen = StrUtil.equalsIgnoreCase(environment.getProperty(SwitchNames.ALIYUN), "true")
                || StrUtil.equalsIgnoreCase(environment.getProperty(SwitchNames.QCLOUD), "true")
                || StrUtil.equalsIgnoreCase(environment.getProperty(SwitchNames.YUNPIAN), "true");
        if (isOpen) {
            return ConditionOutcome.match();
        } else {
            return ConditionOutcome.noMatch("未创建短信推送线程池");
        }
    }
}
