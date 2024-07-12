package com.crypto.recruitmenttest.currencies.data.model

import com.crypto.recruitmenttest.currencies.domain.model.CryptoCurrency
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class CryptoCurrencyDto(
    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "symbol")
    val symbol: String
) {
    fun toDomain() = CryptoCurrency(
        id = id,
        name = name,
        symbol = symbol,
    )
}
