package com.crypto.recruitmenttest.currencylist.ui.screens.list

import com.crypto.recruitmenttest.currencylist.ui.model.CurrencyInfo
import kotlinx.coroutines.flow.Flow

internal interface CurrencyListView {
    fun setDataSource(data: Flow<List<CurrencyInfo>?>)
}
