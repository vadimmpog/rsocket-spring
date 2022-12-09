package com.example.rsocketspring.controller.client

import com.example.rsocketspring.model.entity.MarketData
import org.reactivestreams.Publisher
import org.springframework.http.MediaType
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/client")
class ClientController(
    private val rSocketRequester: RSocketRequester
) {

//    Request-Response
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

//    Fire-and-Forget
    @DeleteMapping("/{name}")
    fun delete(@PathVariable name: String): Mono<Void> {
        return rSocketRequester
            .route("delete")
            .data(name)
            .send()
    }

//    Request-Stream
    @GetMapping("/feed/{name}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun feed(@PathVariable name: String): Publisher<MarketData> {
        return rSocketRequester
            .route("feed")
            .data(name)
            .retrieveFlux(MarketData::class.java)
    }

//     channel
    @GetMapping("/channel")
    fun findUsersByIds(): Flux<MarketData> {
        return rSocketRequester
            .route("channel")
            .data(Flux.range(0, 2), MarketData::class.java)
            .retrieveFlux(MarketData::class.java)
    }

    @GetMapping("/close")
    fun close(){
        rSocketRequester.rsocketClient().dispose()
    }
}