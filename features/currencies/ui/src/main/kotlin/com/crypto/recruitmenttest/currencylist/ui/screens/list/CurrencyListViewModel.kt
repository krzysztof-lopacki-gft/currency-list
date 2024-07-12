package com.crypto.recruitmenttest.currencylist.ui.screens.list

import androidx.lifecycle.viewModelScope
import com.crypto.recruitmenttest.common.ui.mvi.BaseMviViewModel
import com.crypto.recruitmenttest.common.ui.mvi.ViewEffect
import com.crypto.recruitmenttest.currencylist.ui.model.CurrencyInfo
import com.crypto.recruitmenttest.currencylist.ui.screens.list.CurrencyListNavigationEffect.NavigateBack
import com.crypto.recruitmenttest.currencylist.ui.screens.list.CurrencyListViewEvent.OnBackClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.list.CurrencyListViewEvent.OnSearchQueryUpdated
import com.crypto.recruitmenttest.common.ui.mvi.toViewStates
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

internal class CurrencyListViewModel : BaseMviViewModel<CurrencyListViewState, CurrencyListViewEvent, CurrencyListNavigationEffect, ViewEffect>() {
    private val dataSources: MutableStateFlow<Flow<List<CurrencyInfo>?>> = MutableStateFlow(flow { emit(null) })
    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    override val viewStates: StateFlow<CurrencyListViewState> = dataSources
        .flatMapLatest { dataSource -> dataSource }
        .combine(searchQuery) { currencies, query -> currencies to query }
        .mapLatest { data -> CurrenciesData(data.first, data.first?.filterCurrencies(data.second)) }
        .map { currenciesData ->
            with (currenciesData) {
                CurrencyListViewState(
                    isLoadingIndicatorVisible = currencies == null,
                    isNoDataInfoVisible = currencies?.isEmpty() == true,
                    isNoResultsInfoVisible = currencies?.isEmpty() == false && filteredCurrencies?.isEmpty() == true,
                    currencies = filteredCurrencies ?: emptyList()
                )
            }

        }
        .toViewStates(CurrencyListViewState(), viewModelScope)

    override fun onEvent(event: CurrencyListViewEvent) {
        when (event) {
            OnBackClicked -> dispatchNavigationEffect(NavigateBack)
            is OnSearchQueryUpdated -> searchQuery.value = event.query
            is CurrencyListViewEvent.OnDataSourceProvided -> {
                dataSources.value = event.dataSource
            }
        }
    }

    /**
     * [INFO FOR REVIEWER]
     * In the times when using ListView was popular, filtering was often implemented directly within ArrayAdapter,
     * which implemented the Filterable interface. Using this interface, a Filter object was returned
     * and it was responsible for the filtering logic and managing the threads on which these operations took place.
     * It was quite a simple and effective solution, but from an architectural point of view,
     * it is not consistent with the ViewModel pattern, where the ViewModel is fully responsible for preparing the data for the view
     * and the view only displays the passed data.
     * This is even more noticeable in Compose, where the ViewModel maintains even the state used by, for example, text input.
     */
    private suspend fun List<CurrencyInfo>.filterCurrencies(query: String): List<CurrencyInfo> {
        if (query.isEmpty()) return this
        return withContext(Dispatchers.IO) {
            val regex = "\\b${Regex.escape(query)}".toRegex(RegexOption.IGNORE_CASE)
            filter { currencyInfo ->
                if (!isActive) throw CancellationException("Filtering cancelled")
                currencyInfo.symbol.startsWith(query) || regex.find(currencyInfo.name) != null
            }
        }
    }

    private class CurrenciesData(
        val currencies: List<CurrencyInfo>?,
        val filteredCurrencies: List<CurrencyInfo>?,
    )
}
