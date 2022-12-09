package com.example.rsocketspring.repository

import com.example.rsocketspring.model.entity.MarketData
import org.springframework.data.mongodb.repository.MongoRepository


interface MarketDataRepository: MongoRepository<MarketData, String>{
    fun findByName(name: String): MarketData
}