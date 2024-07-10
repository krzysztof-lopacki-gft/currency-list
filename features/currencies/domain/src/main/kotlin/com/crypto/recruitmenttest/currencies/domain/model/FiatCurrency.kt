package com.crypto.recruitmenttest.currencies.domain.model

class FiatCurrency(
    override val id: String,
    override val name: String,
    override val symbol: String,
    val code: String
) : Currency
