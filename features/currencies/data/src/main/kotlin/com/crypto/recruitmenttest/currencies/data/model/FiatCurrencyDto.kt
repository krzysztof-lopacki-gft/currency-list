package com.crypto.recruitmenttest.currencies.data.model

import com.crypto.recruitmenttest.currencies.domain.model.FiatCurrency
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class FiatCurrencyDto(
    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "symbol")
    val symbol: String,

    @Json(name = "code")
    val code: String
) {
    fun toDomain() = FiatCurrency(
        id = id,
        name = name,
        symbol = symbol,
        code = code,
    )
}
