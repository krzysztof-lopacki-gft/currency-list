package com.crypto.recruitmenttest.currencies.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CryptoCurrencyEntity::class, FiatCurrencyEntity::class], version = 1, exportSchema = false)
internal abstract class CurrenciesDatabase : RoomDatabase() {
    abstract fun getCurrenciesDao(): CurrenciesDao
}
