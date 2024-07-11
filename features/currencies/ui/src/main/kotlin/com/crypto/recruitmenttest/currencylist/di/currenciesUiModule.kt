package com.crypto.recruitmenttest.currencylist.di

import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val currenciesUiModule = module {
    viewModelOf(::CurrenciesListDemoViewModel)
}
