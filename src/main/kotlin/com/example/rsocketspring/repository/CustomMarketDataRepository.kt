package com.example.rsocketspring.repository

import com.example.rsocketspring.model.entity.MarketData
import org.springframework.stereotype.Component
import java.util.stream.Stream

@Component
class CustomMarketDataRepository(
    private val repository: MarketDataRepository
) {

    fun findMarketData(): Stream<MarketData> {
        return repository.findAll().stream()
    }

}