package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.OrderDao
import com.example.data.dao.SecurityDao
import com.example.data.dao.WalletDao
import com.example.data.entity.OrderEntity
import com.example.data.entity.SecurityAuditLogEntity
import com.example.data.entity.SecuritySettingsEntity
import com.example.data.entity.WalletAssetEntity

@Database(
    entities = [
        OrderEntity::class,
        WalletAssetEntity::class,
        SecuritySettingsEntity::class,
        SecurityAuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun walletDao(): WalletDao
    abstract fun securityDao(): SecurityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mexc_exchange.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
