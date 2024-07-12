package com.crypto.recruitmenttest.currencies.data.storage

import android.content.Context
import androidx.room.Room
import com.crypto.recruitmenttest.currencies.data.storage.db.CurrenciesDao
import com.crypto.recruitmenttest.currencies.data.storage.db.CurrenciesDatabase
import com.crypto.recruitmenttest.currencies.data.storage.db.toEntity
import com.crypto.recruitmenttest.currencies.domain.model.CryptoCurrency
import com.crypto.recruitmenttest.currencies.domain.model.Currency
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType.Any
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType.Crypto
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType.Fiat
import com.crypto.recruitmenttest.currencies.domain.model.FiatCurrency
import com.crypto.recruitmenttest.currencies.domain.storage.CurrenciesStorage

internal class RoomCurrenciesStorage(
    applicationContext: Context
) : CurrenciesStorage {
    private val dao: CurrenciesDao = Room
        .databaseBuilder(applicationContext, CurrenciesDatabase::class.java, "currencies-database")
        .build()
        .getCurrenciesDao()

    override suspend fun <T : Currency> saveCurrencies(data: List<T>) {
        dao.saveCryptoCurrencies(
            data.asSequence()
                .filterIsInstance<CryptoCurrency>()
                .map { item -> item.toEntity() }
                .toList()
        )
        dao.saveFiatCurrencies(
            data.asSequence()
                .filterIsInstance<FiatCurrency>()
                .map { item -> item.toEntity() }
                .toList()
        )
    }

    @Suppress("UNCHECKED_CAST") // Type is checked with `when(type)`
    override suspend fun <T : Currency> getCurrencies(type: CurrencyType<out T>): List<T> {
        return when(type) {
            Crypto -> dao.getCryptoCurrencies().map { item -> item.toDomain() }
            Fiat -> dao.getFiatCurrencies().map { item -> item.toDomain() }
            Any -> dao.getCryptoCurrencies().map { item -> item.toDomain() } + dao.getFiatCurrencies().map { item -> item.toDomain() }
        } as List<T>
    }

    override suspend fun containsAnyData(): Boolean = dao.containsAnyData()

    override suspend fun clear() = dao.clearAll()
}
