package com.example.rsocketspring.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "marketdata")
data class MarketData (
    @Id
    val name: String,
    val description: String,
    val quantity: Long,
    val price: Float,
)