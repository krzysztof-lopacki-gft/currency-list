package com.crypto.recruitmenttest.di

import com.crypto.recruitmenttest.currencies.di.currenciesDataModule
import com.crypto.recruitmenttest.currencies.di.currenciesDomainModule
import org.koin.dsl.module

val applicationModule = module {
    includes(
        currenciesDomainModule,
        currenciesDataModule,
    )
}
