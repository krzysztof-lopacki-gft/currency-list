package com.crypto.recruitmenttest.currencies.domain.storage

import com.crypto.recruitmenttest.currencies.domain.model.Currency
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType

interface CurrenciesStorage {
    suspend fun <T : Currency> saveCurrencies(data: List<T>)
    suspend fun <T : Currency> getCurrencies(type: CurrencyType<out T>): List<T>
    suspend fun containsAnyData(): Boolean
    suspend fun clear()
}
