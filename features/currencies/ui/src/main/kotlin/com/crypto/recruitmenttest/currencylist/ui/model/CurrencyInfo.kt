package com.crypto.recruitmenttest.currencylist.ui.model

import com.crypto.recruitmenttest.currencies.domain.model.Currency
import com.crypto.recruitmenttest.currencies.domain.model.FiatCurrency

internal data class CurrencyInfo (
    val id: String,
    val name: String,
    val symbol: String,
    val code: String?
)

internal fun Currency.toCurrencyInfo() = CurrencyInfo(
    id = id,
    name = name,
    symbol = symbol,
    code = (this as? FiatCurrency)?.code
)
