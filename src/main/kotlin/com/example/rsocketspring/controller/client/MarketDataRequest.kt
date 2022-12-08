package com.example.rsocketspring.controller.client

data class MarketDataRequest (
    val name: String,
    val description: String,
    val quantity: Long,
    val price: Float
)