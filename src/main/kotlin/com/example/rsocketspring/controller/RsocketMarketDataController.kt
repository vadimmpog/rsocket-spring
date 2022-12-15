package com.example.rsocketspring.controller

import com.example.rsocketspring.controller.client.MarketDataRequest
import com.example.rsocketspring.model.entity.MarketData
import com.example.rsocketspring.service.RsocketService
import com.example.rsocketspring.utils.logger
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux


@Controller
class RsocketMarketDataController(
    private val rsocketService: RsocketService
) {

//    Request-Response
    @Throws(EmptyResultDataAccessException::class)
    @MessageMapping("current")
    fun currentMarketData(@Payload name: String): MarketData? {
        return rsocketService.currentMarketData(name)
    }

    @MessageMapping("create")
    fun createMarketData(@Payload marketDataRequest: MarketDataRequest): MarketData {
        return rsocketService.createMarketData(marketDataRequest)
    }

    @MessageMapping("all")
    fun allMarketData(): List<MarketData> {
        return rsocketService.allMarketData()
    }

//    Fire-and-Forget
    @MessageMapping("delete")
    fun collectMarketData(@Payload name: String) {
        rsocketService.collectMarketData(name)
    }

//    Request-Stream
    @MessageMapping("feed")
    fun feedMarketData(): Flux<MarketData> {
        return rsocketService.feedMarketData()
    }

//    Channel
    @MessageMapping("channel")
    fun echoChannel(payloads: Flux<String>): Flux<MarketData> {
        return rsocketService.echoChannel(payloads)
    }

    @MessageExceptionHandler(EmptyResultDataAccessException::class)
    fun handleException(e: EmptyResultDataAccessException) {
        logger().info("Error occurred!", e)
    }
}