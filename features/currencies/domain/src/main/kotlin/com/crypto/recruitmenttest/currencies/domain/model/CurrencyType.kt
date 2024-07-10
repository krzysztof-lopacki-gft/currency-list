package com.crypto.recruitmenttest.currencies.domain.model

sealed interface CurrencyType<T : Currency> {
    data object Crypto : CurrencyType<CryptoCurrency>
    data object Fiat : CurrencyType<FiatCurrency>
    data object Any : CurrencyType<Currency>
}
