package com.example.data.model

data class CryptoTicker(
    val symbol: String,             // e.g. "BTC/USDT"
    val baseAsset: String,          // "BTC"
    val quoteAsset: String,         // "USDT"
    val price: Double,
    val change24h: Double,          // percentage, e.g. +4.85 or -1.20
    val high24h: Double,
    val low24h: Double,
    val volume24hUsdt: Double,
    val isHot: Boolean = false,
    val isGainer: Boolean = false,
    val category: String = "Spot"  // "Spot", "Futures", "Meme", "Innovation"
)

data class OrderBookEntry(
    val price: Double,
    val amount: Double,
    val total: Double
)

data class OrderBookData(
    val asks: List<OrderBookEntry>, // Sell orders (Red)
    val bids: List<OrderBookEntry>  // Buy orders (Green)
)

data class Candlestick(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
)
