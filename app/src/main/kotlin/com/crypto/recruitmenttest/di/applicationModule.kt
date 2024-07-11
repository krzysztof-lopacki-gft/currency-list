package com.crypto.recruitmenttest.di

import com.crypto.recruitmenttest.currencies.di.currenciesDataModule
import com.crypto.recruitmenttest.currencies.di.currenciesDomainModule
import com.crypto.recruitmenttest.currencylist.di.currenciesUiModule
import org.koin.dsl.module

val applicationModule = module {
    includes(
        currenciesDomainModule,
        currenciesDataModule,
        currenciesUiModule
    )
}
