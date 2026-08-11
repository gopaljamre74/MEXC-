package com.example.data.repository

import com.example.data.dao.OrderDao
import com.example.data.dao.SecurityDao
import com.example.data.dao.WalletDao
import com.example.data.entity.OrderEntity
import com.example.data.entity.SecurityAuditLogEntity
import com.example.data.entity.SecuritySettingsEntity
import com.example.data.entity.WalletAssetEntity
import com.example.data.model.Candlestick
import com.example.data.model.CryptoTicker
import com.example.data.model.OrderBookData
import com.example.data.model.OrderBookEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class ExchangeRepository(
    private val orderDao: OrderDao,
    private val walletDao: WalletDao,
    private val securityDao: SecurityDao
) {
    // Initial tickers feed
    private val initialTickers = listOf(
        CryptoTicker("BTC/USDT", "BTC", "USDT", 94820.50, 3.82, 96200.0, 92100.0, 1824000000.0, isHot = true, isGainer = true, category = "Spot"),
        CryptoTicker("ETH/USDT", "ETH", "USDT", 3480.20, -0.65, 3550.0, 3410.0, 940000000.0, isHot = true, isGainer = false, category = "Spot"),
        CryptoTicker("MX/USDT", "MX", "USDT", 4.125, 12.40, 4.35, 3.65, 84000000.0, isHot = true, isGainer = true, category = "Spot"),
        CryptoTicker("SOL/USDT", "SOL", "USDT", 188.40, 6.15, 192.5, 176.0, 620000000.0, isHot = true, isGainer = true, category = "Spot"),
        CryptoTicker("XRP/USDT", "XRP", "USDT", 2.45, 18.20, 2.60, 2.05, 410000000.0, isHot = true, isGainer = true, category = "Spot"),
        CryptoTicker("PEPE/USDT", "PEPE", "USDT", 0.0000185, 24.50, 0.000021, 0.000014, 210000000.0, isHot = true, isGainer = true, category = "Meme"),
        CryptoTicker("SUI/USDT", "SUI", "USDT", 3.28, 4.10, 3.40, 3.10, 140000000.0, category = "Innovation"),
        CryptoTicker("BTC/USDT 100X", "BTC", "USDT", 94850.00, 4.10, 96500.0, 92000.0, 3500000000.0, isHot = true, category = "Futures"),
        CryptoTicker("ETH/USDT 75X", "ETH", "USDT", 3482.10, -0.50, 3560.0, 3400.0, 1800000000.0, isHot = true, category = "Futures"),
        CryptoTicker("DOGE/USDT", "DOGE", "USDT", 0.385, -2.10, 0.410, 0.370, 180000000.0, category = "Meme")
    )

    // Flow that emits live real-time price updates every 600ms
    fun getLiveTickersFlow(): Flow<List<CryptoTicker>> = flow {
        var currentList = initialTickers
        emit(currentList)
        while (true) {
            delay(600)
            currentList = currentList.map { ticker ->
                val deltaPercent = (Random.nextDouble(-0.35, 0.38))
                val newPrice = (ticker.price * (1 + deltaPercent / 100.0)).let {
                    if (it < 1.0) String.format("%.6f", it).toDouble()
                    else String.format("%.2f", it).toDouble()
                }
                val newChange = String.format("%.2f", ticker.change24h + (deltaPercent * 0.1)).toDouble()
                ticker.copy(price = newPrice, change24h = newChange)
            }
            emit(currentList)
        }
    }

    // Generate real-time OrderBook depth
    fun getOrderBook(currentPrice: Double): OrderBookData {
        val asks = mutableListOf<OrderBookEntry>()
        val bids = mutableListOf<OrderBookEntry>()
        var cumAsk = 0.0
        var cumBid = 0.0

        for (i in 1..7) {
            val askPrice = currentPrice * (1 + (i * 0.0008))
            val askAmt = Random.nextDouble(0.15, 2.5)
            cumAsk += askAmt
            asks.add(OrderBookEntry(askPrice, askAmt, cumAsk))

            val bidPrice = currentPrice * (1 - (i * 0.0008))
            val bidAmt = Random.nextDouble(0.15, 2.5)
            cumBid += bidAmt
            bids.add(OrderBookEntry(bidPrice, bidAmt, cumBid))
        }
        return OrderBookData(asks.reversed(), bids)
    }

    // Generate K-Line candlestick data
    fun getCandlesticks(basePrice: Double): List<Candlestick> {
        val candles = mutableListOf<Candlestick>()
        var price = basePrice * 0.95
        val now = System.currentTimeMillis()
        for (i in 30 downTo 0) {
            val open = price
            val change = Random.nextDouble(-0.012, 0.015) * open
            val close = open + change
            val high = maxOf(open, close) + Random.nextDouble(0.001, 0.005) * open
            val low = minOf(open, close) - Random.nextDouble(0.001, 0.005) * open
            val volume = Random.nextDouble(100.0, 1500.0)
            candles.add(Candlestick(now - i * 60000L, open, high, low, close, volume))
            price = close
        }
        return candles
    }

    // Database operations
    fun getAllOrders(): Flow<List<OrderEntity>> = orderDao.getAllOrders()

    suspend fun executeLowLatencyTrade(
        symbol: String,
        type: String,
        orderMode: String,
        price: Double,
        amount: Double
    ): OrderEntity {
        val startTime = System.nanoTime()
        // Simulate high-speed matching engine processing delay (< 20ms)
        val engineProcessingMs = Random.nextInt(8, 19)
        delay(engineProcessingMs.toLong())

        val executionLatency = ((System.nanoTime() - startTime) / 1_000_000).toInt()
        val totalUsdt = price * amount

        val newOrder = OrderEntity(
            symbol = symbol,
            type = type,
            orderMode = orderMode,
            price = price,
            amount = amount,
            totalUsdt = totalUsdt,
            status = "FILLED",
            executionLatencyMs = maxOf(1, executionLatency)
        )

        orderDao.insertOrder(newOrder)

        // Update Wallet balance automatically
        val baseAsset = symbol.split("/").firstOrNull() ?: "BTC"
        val existingBase = walletDao.getAsset(baseAsset) ?: WalletAssetEntity(baseAsset, 0.0, 0.0)
        val existingUsdt = walletDao.getAsset("USDT") ?: WalletAssetEntity("USDT", 100000.0, 500000.0)

        if (type == "BUY") {
            walletDao.insertOrUpdateAsset(existingBase.copy(hotBalance = existingBase.hotBalance + amount))
            walletDao.insertOrUpdateAsset(existingUsdt.copy(hotBalance = maxOf(0.0, existingUsdt.hotBalance - totalUsdt)))
        } else {
            walletDao.insertOrUpdateAsset(existingBase.copy(hotBalance = maxOf(0.0, existingBase.hotBalance - amount)))
            walletDao.insertOrUpdateAsset(existingUsdt.copy(hotBalance = existingUsdt.hotBalance + totalUsdt))
        }

        return newOrder
    }

    fun getWalletAssets(): Flow<List<WalletAssetEntity>> = walletDao.getAllAssets()

    suspend fun initializeDefaultWallet() {
        val defaultAssets = listOf(
            WalletAssetEntity("USDT", 48500.00, 250000.00),
            WalletAssetEntity("BTC", 1.4500, 10.0000),
            WalletAssetEntity("ETH", 12.8000, 50.0000),
            WalletAssetEntity("MX", 8500.00, 20000.00),
            WalletAssetEntity("SOL", 140.00, 500.00)
        )
        walletDao.insertAll(defaultAssets)
    }

    suspend fun transferToColdStorage(assetSymbol: String, amount: Double): Boolean {
        val currentAsset = walletDao.getAsset(assetSymbol) ?: return false
        if (currentAsset.hotBalance < amount) return false

        val updated = currentAsset.copy(
            hotBalance = currentAsset.hotBalance - amount,
            coldStorageBalance = currentAsset.coldStorageBalance + amount
        )
        walletDao.insertOrUpdateAsset(updated)

        securityDao.insertAuditLog(
            SecurityAuditLogEntity(
                action = "Cold Storage Vault Deposit: $amount $assetSymbol",
                ipAddress = "192.168.1.104 (Encrypted VPN)",
                deviceName = "MEXC Mobile Android Secure Hardware Enclave",
                status = "COMPLETED_COLD_ISOLATION"
            )
        )
        return true
    }

    fun getSecuritySettings(): Flow<SecuritySettingsEntity?> = securityDao.getSecuritySettings()

    suspend fun updateSecuritySettings(settings: SecuritySettingsEntity) {
        securityDao.updateSecuritySettings(settings)
    }

    fun getAuditLogs(): Flow<List<SecurityAuditLogEntity>> = securityDao.getAuditLogs()

    suspend fun initializeDefaultSecurity() {
        securityDao.updateSecuritySettings(
            SecuritySettingsEntity(
                id = 1,
                isTwoFactorEnabled = true,
                secretTotpKey = "MEXC-9921-X81A-4432",
                antiPhishingCode = "MEXC_PROT_2026",
                isBiometricsEnabled = true,
                coldStorageVaultLocked = true,
                kycLevel = "KYC Level 2 Advanced Verified"
            )
        )
        securityDao.insertAuditLog(
            SecurityAuditLogEntity(
                action = "App Initialized with 2FA & HSM Enclave Protection",
                ipAddress = "10.0.2.15",
                deviceName = "Pixel 8 Pro Android Enclave",
                status = "SECURE"
            )
        )
    }
}
