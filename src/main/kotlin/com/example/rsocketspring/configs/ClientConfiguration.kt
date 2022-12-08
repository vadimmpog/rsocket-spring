package com.example.rsocketspring.configs

import io.rsocket.core.RSocketConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.util.MimeTypeUtils
import reactor.util.retry.Retry
import java.time.Duration


@Configuration
class ClientConfiguration(
    private val rSocketStrategies: RSocketStrategies
) {
    @Bean
    fun rSocketRequester(): RSocketRequester {
        val builder = RSocketRequester.builder()

        return builder.rsocketConnector { rSocketConnector: RSocketConnector ->
            rSocketConnector.reconnect(
                Retry.fixedDelay(2, Duration.ofSeconds(2))
            )
        }
            .rsocketStrategies(rSocketStrategies)
            .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
            .tcp("localhost", 3000)
    }
}