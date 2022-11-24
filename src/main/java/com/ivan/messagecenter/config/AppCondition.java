package com.ivan.messagecenter.config;

import cn.hutool.core.util.StrUtil;
import com.ivan.messagecenter.config.property.SwitchNames;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * App推送配置判断
 *
 * @author yingfan.cui@dezhentech.com
 * @date 2022/11/24 14:04:27
 */
public class AppCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        boolean isOpen = StrUtil.equalsIgnoreCase(environment.getProperty(SwitchNames.JPUSH), "true")
                || StrUtil.equalsIgnoreCase(environment.getProperty(SwitchNames.GETUI), "true");
        if (isOpen) {
            return ConditionOutcome.match();
        } else {
            return ConditionOutcome.noMatch("未创建APP推送线程池");
        }
    }
}
