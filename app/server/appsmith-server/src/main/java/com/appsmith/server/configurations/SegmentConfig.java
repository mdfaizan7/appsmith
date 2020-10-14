package com.appsmith.server.configurations;

import com.segment.analytics.Analytics;
import com.segment.analytics.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnExpression(value = "!'${segment.writeKey:}'.isEmpty()")
public class SegmentConfig {

    @Value("${segment.writeKey}")
    private String writeKey;

    @Bean
    public Analytics analyticsRunner() {
        final Log logProcessor = new Log() {
            private final String PREFIX = "SEGMENT:";

            @Override
            public void print(Level level, String format, Object... args) {
                log.info(PREFIX + level + ":\t" + String.format(format, args));
            }

            @Override
            public void print(Level level, Throwable error, String format, Object... args) {
                log.info(PREFIX + level + ":\t" + String.format(format, args));
                log.error(PREFIX, error);
            }
        };

        return Analytics.builder(writeKey).log(logProcessor).build();
    }

}
