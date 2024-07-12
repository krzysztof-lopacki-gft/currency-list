package com.crypto.recruitmenttest.currencylist.ui.screens.example

import androidx.annotation.StringRes
import com.crypto.recruitmenttest.common.ui.mvi.NavigationEffect
import com.crypto.recruitmenttest.common.ui.mvi.ViewEffect
import com.crypto.recruitmenttest.common.ui.mvi.ViewEvent
import com.crypto.recruitmenttest.common.ui.mvi.ViewState
import com.crypto.recruitmenttest.currencylist.ui.model.CurrencyInfo

internal data class CurrenciesListDemoViewState(
    val currenciesList: List<CurrencyInfo>?
) : ViewState

internal sealed interface CurrenciesListDemoViewEvent : ViewEvent {
    data object OnClearDataClicked : CurrenciesListDemoViewEvent
    data object OnLoadDataClicked : CurrenciesListDemoViewEvent
    data object OnShowCryptoCurrenciesClicked : CurrenciesListDemoViewEvent
    data object OnShowFiatCurrenciesClicked : CurrenciesListDemoViewEvent
    data object OnShowAllCurrenciesClicked : CurrenciesListDemoViewEvent
}

internal sealed interface CurrenciesListDemoNavigationEffect : NavigationEffect {
    data object NavigateToCurrenciesList : CurrenciesListDemoNavigationEffect
}

internal sealed interface CurrenciesListDemoViewEffect : ViewEffect {
    data class ShowToast(@StringRes val message: Int) : CurrenciesListDemoViewEffect
}
