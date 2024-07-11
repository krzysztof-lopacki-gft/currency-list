package com.crypto.recruitmenttest.currencies.domain.usecases

import com.crypto.recruitmenttest.currencies.domain.repository.CurrenciesRepository

class ClearCurrenciesDataUseCase internal constructor(
    private val currenciesRepository: CurrenciesRepository,
) {
    suspend operator fun invoke() = currenciesRepository.clear()
}
