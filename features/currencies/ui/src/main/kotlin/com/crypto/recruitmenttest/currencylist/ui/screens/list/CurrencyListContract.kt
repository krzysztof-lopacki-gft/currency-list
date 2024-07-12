package com.crypto.recruitmenttest.currencylist.ui.screens.list

import com.crypto.recruitmenttest.common.ui.mvi.NavigationEffect
import com.crypto.recruitmenttest.common.ui.mvi.ViewEvent
import com.crypto.recruitmenttest.common.ui.mvi.ViewState
import com.crypto.recruitmenttest.currencylist.ui.model.CurrencyInfo
import kotlinx.coroutines.flow.Flow

internal data class CurrencyListViewState(
    val currencies: List<CurrencyInfo> = emptyList(),
    val isLoadingIndicatorVisible: Boolean = true,
    val isNoDataInfoVisible: Boolean = false,
    val isEmptySearchResultInfoVisible: Boolean = false
) : ViewState

internal sealed interface CurrencyListViewEvent : ViewEvent {
    data class OnSearchQueryUpdated(val query: String) : CurrencyListViewEvent
    data object OnBackClicked : CurrencyListViewEvent
    data class OnDataSourceProvided(val dataSource: Flow<List<CurrencyInfo>?>) : CurrencyListViewEvent
}

internal sealed interface CurrencyListNavigationEffect : NavigationEffect {
    data object NavigateBack : CurrencyListNavigationEffect
}
