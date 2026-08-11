package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val symbol: String,
    val type: String,               // "BUY" or "SELL"
    val orderMode: String,          // "LIMIT", "MARKET", or "FUTURES_10X"
    val price: Double,
    val amount: Double,
    val totalUsdt: Double,
    val status: String,             // "FILLED", "PENDING", "CANCELLED"
    val executionLatencyMs: Int,    // Low-latency execution stat, e.g., 14ms
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "wallet_assets")
data class WalletAssetEntity(
    @PrimaryKey val assetSymbol: String, // "BTC", "ETH", "USDT", "MX", "SOL"
    val hotBalance: Double,
    val coldStorageBalance: Double,
    val lockedInOrders: Double = 0.0
)

@Entity(tableName = "security_settings")
data class SecuritySettingsEntity(
    @PrimaryKey val id: Int = 1,
    val isTwoFactorEnabled: Boolean = true,
    val secretTotpKey: String = "MEXC-88A9-44F1-77BC",
    val antiPhishingCode: String = "MEXC_SAFE_2026",
    val isBiometricsEnabled: Boolean = true,
    val coldStorageVaultLocked: Boolean = true,
    val kycLevel: String = "KYC Level 2 Advanced"
)

@Entity(tableName = "security_audit_logs")
data class SecurityAuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val action: String,
    val ipAddress: String,
    val deviceName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String
)
