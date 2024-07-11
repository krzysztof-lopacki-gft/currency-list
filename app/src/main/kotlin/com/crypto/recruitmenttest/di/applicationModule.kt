package com.crypto.recruitmenttest.di

import com.crypto.recruitmenttest.currencies.di.currenciesDataModule
import org.koin.dsl.module

val applicationModule = module {
    includes(
        currenciesDataModule
    )
}
