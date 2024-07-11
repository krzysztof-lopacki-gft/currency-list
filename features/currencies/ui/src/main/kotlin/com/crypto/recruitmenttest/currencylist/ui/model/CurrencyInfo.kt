package com.crypto.recruitmenttest.currencylist.ui.model

import com.crypto.recruitmenttest.currencies.domain.model.Currency
import com.crypto.recruitmenttest.currencies.domain.model.FiatCurrency

data class CurrencyInfo (
    val id: String,
    val name: String,
    val symbol: String,
    val code: String?
)

fun Currency.toCurrencyInfo() = CurrencyInfo(
    id = id,
    name = name,
    symbol = symbol,
    code = (this as? FiatCurrency)?.code
)
