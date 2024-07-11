package com.crypto.recruitmenttest.currencylist.ui.screens.example

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.crypto.recruitmenttest.common.ui.mvi.BaseMviViewModel
import com.crypto.recruitmenttest.common.ui.mvi.ViewEffect
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType
import com.crypto.recruitmenttest.currencies.domain.usecases.ClearCurrenciesDataUseCase
import com.crypto.recruitmenttest.currencies.domain.usecases.GetCurrenciesUseCase
import com.crypto.recruitmenttest.currencies.domain.usecases.LoadCurrenciesDataUseCase
import com.crypto.recruitmenttest.currencylist.ui.model.toCurrencyInfo
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoNavigationEffect.NavigateToCurrenciesList
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEvent.OnClearDataClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEvent.OnLoadDataClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEvent.OnShowAllCurrenciesClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEvent.OnShowCryptoCurrenciesClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEvent.OnShowFiatCurrenciesClicked
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch

private const val CURRENCY_FILTER_KEY = "CurrenciesListDemoViewModel.currencyFilter"

class CurrenciesListDemoViewModel internal constructor(
    savedStateHandle: SavedStateHandle,
    private val clearCurrenciesData: ClearCurrenciesDataUseCase,
    private val loadCurrenciesData: LoadCurrenciesDataUseCase,
    private val getCurrencies: GetCurrenciesUseCase,
) : BaseMviViewModel<CurrenciesListDemoViewState, CurrenciesListDemoViewEvent, CurrenciesListDemoNavigationEffect, ViewEffect>(
    initialState = CurrenciesListDemoViewState(
        currenciesList = null
    )
) {
    private val currencyFilter = savedStateHandle.getLiveData<CurrencyFilter?>(CURRENCY_FILTER_KEY, null)

    init {
        currencyFilter.value?.let { filter ->
            updateCurrenciesFilter(filter)
        }
    }

    override fun onEvent(event: CurrenciesListDemoViewEvent) {
        when(event) {
            OnClearDataClicked -> launchInDbContext { clearCurrenciesData() }
            OnLoadDataClicked -> launchInDbContext { loadCurrenciesData() }
            OnShowCryptoCurrenciesClicked -> updateCurrenciesFilterAndNavigateToCurrenciesList(CurrencyFilter.CRYPTO)
            OnShowFiatCurrenciesClicked -> updateCurrenciesFilterAndNavigateToCurrenciesList(CurrencyFilter.FIAT)
            OnShowAllCurrenciesClicked -> updateCurrenciesFilterAndNavigateToCurrenciesList(CurrencyFilter.ALL)
        }
    }

    private fun updateCurrenciesFilterAndNavigateToCurrenciesList(newCurrencyFilter: CurrencyFilter) {
        updateCurrenciesFilter(newCurrencyFilter)
        dispatchNavigationEffect(NavigateToCurrenciesList)
    }

    private fun updateCurrenciesFilter(newCurrencyFilter: CurrencyFilter) {
        viewState = CurrenciesListDemoViewState(
            currenciesList = null
        )
        currencyFilter.value = newCurrencyFilter
        launchInDbContext {
            viewState = CurrenciesListDemoViewState(
                currenciesList = getCurrencies(newCurrencyFilter.toCurrencyType()).map { it.toCurrencyInfo() }
            )
        }
    }

    private fun launchInDbContext(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(
        context = Dispatchers.IO + NonCancellable,
        block = block
    )

    private enum class CurrencyFilter {
        CRYPTO,
        FIAT,
        ALL;

        fun toCurrencyType() = when (this) {
            CRYPTO -> CurrencyType.Crypto
            FIAT -> CurrencyType.Fiat
            ALL -> CurrencyType.Any
        }
    }
}

