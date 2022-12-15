package com.example.rsocketspring.service

import com.example.rsocketspring.controller.client.MarketDataRequest
import com.example.rsocketspring.model.entity.MarketData
import com.example.rsocketspring.repository.CustomMarketDataRepository
import com.example.rsocketspring.repository.MarketDataRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration.ofMillis

@Service
class RsocketService(
    private val marketDataRepository: MarketDataRepository,
    private val customMarketDataRepository: CustomMarketDataRepository
) {


    fun currentMarketData(name: String): MarketData? = marketDataRepository.findByName(name)

    fun createMarketData(marketDataRequest: MarketDataRequest): MarketData {
        return marketDataRepository.save(
            MarketData(
                marketDataRequest.name,
                marketDataRequest.description,
                marketDataRequest.quantity,
                marketDataRequest.price
            )
        )
    }

    fun allMarketData(): List<MarketData> = marketDataRepository.findAll()

    fun collectMarketData(name: String) {
        val marketData = marketDataRepository.findByName(name)
        marketDataRepository.delete(marketData)
    }

    fun feedMarketData(): Flux<MarketData> = Flux.fromStream(customMarketDataRepository.findMarketData())

    fun echoChannel(payloads: Flux<String>): Flux<MarketData> =
        payloads.delayElements(ofMillis(10)).map { payload: String -> marketDataRepository.findByName(payload) }

}