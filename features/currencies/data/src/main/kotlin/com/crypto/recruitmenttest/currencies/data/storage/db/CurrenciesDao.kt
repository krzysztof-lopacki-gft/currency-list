package com.crypto.recruitmenttest.currencies.data.storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
internal interface CurrenciesDao {

    @Query("SELECT * FROM crypto_currencies")
    fun getCryptoCurrencies(): List<CryptoCurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCryptoCurrencies(currencies: List<CryptoCurrencyEntity>)

    @Query("SELECT * FROM fiat_currencies")
    fun getFiatCurrencies(): List<FiatCurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFiatCurrencies(currencies: List<FiatCurrencyEntity>)

    @Query("DELETE FROM crypto_currencies")
    fun clearCryptoCurrencies()

    @Query("DELETE FROM fiat_currencies")
    fun clearFiatCurrencies()

    @Query("SELECT COUNT(1) from crypto_currencies")
    fun containsAnyCryptoCurrencies(): Boolean

    @Query("SELECT COUNT(1) from fiat_currencies")
    fun containsAnyFiatCurrencies(): Boolean

    @Transaction
    fun containsAnyData() = containsAnyCryptoCurrencies() || containsAnyFiatCurrencies()

    @Transaction
    fun clearAll() {
        clearCryptoCurrencies()
        clearFiatCurrencies()
    }
}
