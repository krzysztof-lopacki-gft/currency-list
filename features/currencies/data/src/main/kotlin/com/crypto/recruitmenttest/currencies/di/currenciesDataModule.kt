package com.crypto.recruitmenttest.currencies.di

import com.crypto.recruitmenttest.currencies.data.services.FileBasedCurrenciesFetcher
import com.crypto.recruitmenttest.currencies.domain.services.CurrenciesFetcher
import com.squareup.moshi.Moshi
import org.koin.dsl.module

val currenciesDataModule = module {
    single<Moshi> {
        Moshi.Builder().build()
    }

    single<CurrenciesFetcher> {
        FileBasedCurrenciesFetcher(get(), get())
    }
}
