package com.crypto.recruitmenttest.currencies.domain.model

/**
 * [INFO FOR REVIEWER]
 * Having `Currency`, `CryptoCurrency` and `FiatCurrency` instead of one `CurrencyInfo` model with nullable `code: String` may seem like an overkill,
 * but this way we are prepared for handling more differences between crypto and fiat currencies.
 */
interface Currency {
    val id: String
    val name: String
    val symbol: String
}
