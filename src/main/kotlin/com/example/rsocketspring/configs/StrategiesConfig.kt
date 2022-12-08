package com.example.rsocketspring.configs

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.util.ClassUtils
import org.springframework.web.util.pattern.PathPatternRouteMatcher

@Configuration
class StrategiesConfig {
    private val PATHPATTERN_ROUTEMATCHER_CLASS = "org.springframework.web.util.pattern.PathPatternRouteMatcher"

    @Bean
    @ConditionalOnMissingBean
    fun rSocketStrategies(customizers: ObjectProvider<RSocketStrategiesCustomizer>): RSocketStrategies? {
        val builder = RSocketStrategies.builder()
        if (ClassUtils.isPresent(PATHPATTERN_ROUTEMATCHER_CLASS, null)) {
            builder.routeMatcher(PathPatternRouteMatcher())
        }
        customizers.orderedStream().forEach { customizer: RSocketStrategiesCustomizer ->
            customizer.customize(
                builder
            )
        }
        return builder.build()
    }
}