package com.example.rsocketspring.controller

import com.example.rsocketspring.controller.client.MarketDataRequest
import com.example.rsocketspring.model.entity.MarketData
import com.example.rsocketspring.repository.CustomMarketDataRepository
import com.example.rsocketspring.repository.MarketDataRepository
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration.ofMillis


@Controller
class RsocketMarketDataController(
    private val marketDataRepository: MarketDataRepository,
    private val customMarketDataRepository: CustomMarketDataRepository
) {

//    Request-Response
    @MessageMapping("current")
    fun currentMarketData(@Payload name: String): MarketData? {
        return marketDataRepository.findByName(name)
    }

    @MessageMapping("create")
    fun createMarketData(@Payload marketDataRequest: MarketDataRequest): MarketData {
        return marketDataRepository.save(MarketData(marketDataRequest.name, marketDataRequest.description, marketDataRequest.quantity, marketDataRequest.price))
    }

    @MessageMapping("all")
    fun allMarketData(): List<MarketData> {
        return marketDataRepository.findAll()
    }

//    Fire-and-Forget
    @MessageMapping("delete")
    fun collectMarketData(@Payload name: String) {
        val marketData = marketDataRepository.findByName(name)
        marketDataRepository.delete(marketData)
    }

//    Request-Stream
    @MessageMapping("feed")
    fun feedMarketData(@Payload name: String): Flux<MarketData> {
        return Flux.fromStream(customMarketDataRepository.findMarketPrice(name))
    }

//    Channel
    @MessageMapping("channel")
    fun echoChannel(payloads: Flux<String>): Flux<MarketData> {
        return payloads.delayElements(ofMillis(10)).map { payload: String -> marketDataRepository.findByName(payload) }}

    @MessageExceptionHandler
    fun handleException(e: Exception): Mono<String> {
        return Mono.just("Error occurred: ${e.message.toString()}")
    }
}