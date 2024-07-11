package com.crypto.recruitmenttest.currencies.di

import com.crypto.recruitmenttest.currencies.domain.repository.CurrenciesRepository
import com.crypto.recruitmenttest.currencies.domain.usecases.ClearCurrenciesDataUseCase
import com.crypto.recruitmenttest.currencies.domain.usecases.GetCurrenciesUseCase
import com.crypto.recruitmenttest.currencies.domain.usecases.LoadCurrenciesDataUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val currenciesDomainModule = module {
    singleOf(::CurrenciesRepository)
    factoryOf(::ClearCurrenciesDataUseCase)
    factoryOf(::LoadCurrenciesDataUseCase)
    factoryOf(::GetCurrenciesUseCase)
}
