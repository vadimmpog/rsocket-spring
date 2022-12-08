package com.example.rsocketspring.controller

import com.example.rsocketspring.controller.client.MarketDataRequest
import com.example.rsocketspring.model.entity.MarketData
import com.example.rsocketspring.repository.MarketDataRepository
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller


@Controller
class RsocketMarketDataController(
    private val marketDataRepository: MarketDataRepository
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
    fun all(): List<MarketData> {
        return marketDataRepository.findAll()
    }

//    Fire-and-Forget
    @MessageMapping("delete")
    fun collectMarketData(@Payload name: String) {
        val marketData = marketDataRepository.findByName(name)
        marketDataRepository.delete(marketData)
    }

////    Request-Stream
//    @MessageMapping("feedMarketData")
//    fun feedMarketData(marketDataRequest: MarketDataRequest): Flux<MarketData?>? {
//        return marketDataRepository.getAll(marketDataRequest.getStock())
//    }

//    Channel
//    @MessageMapping("channel")
//    fun channel(settings: Flux<Duration?>): Flux<Message?>? {
//        return settings
//            .doOnNext(Consumer<Duration> { setting: Duration -> log.info("\nFrequency setting is {} second(s).\n", setting.getSeconds()) })
//            .switchMap(Function<Duration, Publisher<*>> { setting: Duration? ->
//                Flux.interval(setting)
//                    .map<Any> { index: Long? ->
//                        Message(
//                            SERVER,
//                            CHANNEL,
//                            index
//                        )
//                    }
//            })
//            .log()
//    }

}