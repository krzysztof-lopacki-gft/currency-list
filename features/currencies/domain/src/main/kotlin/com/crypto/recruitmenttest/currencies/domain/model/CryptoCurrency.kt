package com.crypto.recruitmenttest.currencies.domain.model

data class CryptoCurrency(
    override val id: String,
    override val name: String,
    override val symbol: String
) : Currency
