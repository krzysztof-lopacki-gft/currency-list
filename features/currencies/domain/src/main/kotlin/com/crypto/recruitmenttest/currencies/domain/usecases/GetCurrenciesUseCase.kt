package com.crypto.recruitmenttest.currencies.domain.usecases

import com.crypto.recruitmenttest.currencies.domain.model.Currency
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType
import com.crypto.recruitmenttest.currencies.domain.repository.CurrenciesRepository

class GetCurrenciesUseCase internal constructor(
    private val currenciesRepository: CurrenciesRepository,
) {
    suspend operator fun <T : Currency> invoke(type: CurrencyType<T>) = currenciesRepository.getCurrencies(type)
}
