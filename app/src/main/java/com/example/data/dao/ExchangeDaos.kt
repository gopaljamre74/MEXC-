package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.OrderEntity
import com.example.data.entity.SecurityAuditLogEntity
import com.example.data.entity.SecuritySettingsEntity
import com.example.data.entity.WalletAssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM user_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("DELETE FROM user_orders WHERE id = :id")
    suspend fun cancelOrder(id: Long)
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_assets")
    fun getAllAssets(): Flow<List<WalletAssetEntity>>

    @Query("SELECT * FROM wallet_assets WHERE assetSymbol = :symbol LIMIT 1")
    suspend fun getAsset(symbol: String): WalletAssetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAsset(asset: WalletAssetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assets: List<WalletAssetEntity>)
}

@Dao
interface SecurityDao {
    @Query("SELECT * FROM security_settings WHERE id = 1 LIMIT 1")
    fun getSecuritySettings(): Flow<SecuritySettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSecuritySettings(settings: SecuritySettingsEntity)

    @Query("SELECT * FROM security_audit_logs ORDER BY timestamp DESC LIMIT 20")
    fun getAuditLogs(): Flow<List<SecurityAuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: SecurityAuditLogEntity)
}
