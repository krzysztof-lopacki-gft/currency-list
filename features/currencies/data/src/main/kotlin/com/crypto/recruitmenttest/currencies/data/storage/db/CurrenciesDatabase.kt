package com.crypto.recruitmenttest.currencies.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CryptoCurrencyEntity::class, FiatCurrencyEntity::class], version = 1, exportSchema = false)
abstract class CurrenciesDatabase : RoomDatabase() {
    abstract fun getCurrenciesDao(): CurrenciesDao
}
