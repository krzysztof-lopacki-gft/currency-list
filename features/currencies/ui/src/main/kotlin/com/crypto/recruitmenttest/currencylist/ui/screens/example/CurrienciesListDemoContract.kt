package com.crypto.recruitmenttest.currencylist.ui.screens.example

import com.crypto.recruitmenttest.common.ui.mvi.NavigationEffect
import com.crypto.recruitmenttest.common.ui.mvi.ViewEvent
import com.crypto.recruitmenttest.common.ui.mvi.ViewState
import com.crypto.recruitmenttest.currencylist.ui.model.CurrencyInfo

data class CurrenciesListDemoViewState(
    val currenciesList: List<CurrencyInfo>?
) : ViewState

sealed interface CurrenciesListDemoViewEvent : ViewEvent {
    data object OnClearDataClicked : CurrenciesListDemoViewEvent
    data object OnLoadDataClicked : CurrenciesListDemoViewEvent
    data object OnShowCryptoCurrenciesClicked : CurrenciesListDemoViewEvent
    data object OnShowFiatCurrenciesClicked : CurrenciesListDemoViewEvent
    data object OnShowAllCurrenciesClicked : CurrenciesListDemoViewEvent
}

sealed interface CurrenciesListDemoNavigationEffect : NavigationEffect {
    data object NavigateToCurrenciesList : CurrenciesListDemoNavigationEffect
}
