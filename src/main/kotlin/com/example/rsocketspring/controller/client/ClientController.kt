package com.example.rsocketspring.controller.client

import com.example.rsocketspring.model.entity.MarketData
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/client")
class ClientController(
    private val rSocketRequester: RSocketRequester
) {

    @GetMapping(value = ["/current/{name}"])
    fun current(@PathVariable("name") name: String): Mono<MarketData> {
        return rSocketRequester
            .route("current")
            .data(name)
            .retrieveMono(MarketData::class.java)
    }

    @GetMapping("/create")
    fun sendRequestResponseAll(@RequestBody marketDataRequest: MarketDataRequest): Mono<MarketData> {
        return rSocketRequester
            .route("create")
            .data(marketDataRequest)
            .retrieveMono(MarketData::class.java)
    }

    @GetMapping("/all")
    fun all(): Mono<Array<MarketData>> {
        return rSocketRequester
            .route("all")
            .retrieveMono(Array<MarketData>::class.java)
    }

    @DeleteMapping("/{name}")
    fun delete(@PathVariable name: String): Mono<Void> {
        return rSocketRequester
            .route("delete")
            .data(name)
            .send()
    }


}