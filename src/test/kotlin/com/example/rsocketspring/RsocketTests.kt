package com.example.rsocketspring

import com.example.rsocketspring.controller.client.MarketDataRequest
import com.example.rsocketspring.model.entity.MarketData
import com.example.rsocketspring.repository.CustomMarketDataRepository
import com.example.rsocketspring.repository.MarketDataRepository
import com.example.rsocketspring.service.RsocketService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import reactor.core.publisher.Flux
import java.util.stream.Stream

@SpringBootTest
class RsocketTests {
    private val marketDataRepository: MarketDataRepository = mockk()
    private val customMarketDataRepository: CustomMarketDataRepository = mockk()
    private val rsocketService: RsocketService = RsocketService(marketDataRepository, customMarketDataRepository)

    val testMarketItem = MarketData(
        name = "Chipboard",
        price = 520.25F,
        quantity = 5,
        description = "Just test item"
    )
    val newTestMarketItem = MarketData(
        name = "Metal scrap",
        price = 60F,
        quantity = 100,
        description = "Just new test item"
    )

    @Test
    fun currentMarketDataTest() {
        every { marketDataRepository.findByName("Chipboard") } returns testMarketItem
        val name = "Chipboard"
        val item = rsocketService.currentMarketData(name)
        assert(item != null)
        assert(item?.quantity == testMarketItem.quantity)
    }

    @Test
    fun createMarketDataTest() {
        val newItem = MarketDataRequest(
            name = "Metal scrap",
            price = 60F,
            quantity = 100,
            description = "Just new test item"
        )
        every {
            marketDataRepository.save(
                MarketData(
                    name = "Metal scrap",
                    price = 60F,
                    quantity = 100,
                    description = "Just new test item"
                )
            )
        } returns newTestMarketItem
        val item = rsocketService.createMarketData(newItem)
        assert(item.name == newTestMarketItem.name)
    }

    @Test
    fun allMarketDataTest() {
        every { marketDataRepository.findAll() } returns listOf(testMarketItem, newTestMarketItem)
        val lst = rsocketService.allMarketData()
        assert(lst.size == 2)
    }

    @Test
    fun feedMarketDataTest() {
        every { customMarketDataRepository.findMarketData() } returns Stream.of(testMarketItem, newTestMarketItem)

        val fMarketData = rsocketService.feedMarketData()
        println(fMarketData)
    }

    @Test
    fun echoChannelTest() {
        val payloads: Flux<String> = Flux.just("Chipboard", "Metal scrap")
        every { marketDataRepository.findByName("Chipboard") } returns testMarketItem
        every { marketDataRepository.findByName("Metal scrap") } returns newTestMarketItem

        val fMarketData = rsocketService.echoChannel(payloads)
        println(fMarketData)
    }

}
