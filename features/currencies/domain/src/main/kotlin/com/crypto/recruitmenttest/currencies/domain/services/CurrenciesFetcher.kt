package com.crypto.recruitmenttest.currencies.domain.services

import com.crypto.recruitmenttest.currencies.domain.model.Currency
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType

interface CurrenciesFetcher {

    suspend fun <T : Currency> fetchCurrencies(type: CurrencyType<out T>): List<T>
}


