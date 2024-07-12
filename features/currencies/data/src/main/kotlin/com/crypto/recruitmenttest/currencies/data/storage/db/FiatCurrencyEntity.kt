package com.crypto.recruitmenttest.currencies.data.storage.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crypto.recruitmenttest.currencies.domain.model.FiatCurrency

@Entity(tableName = "fiat_currencies")
internal class FiatCurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val dbId: Long?,
    val id: String,
    val name: String,
    val symbol: String,
    val code: String,
) {
    fun toDomain() = FiatCurrency(
        id = id,
        name = name,
        symbol = symbol,
        code = code
    )
}

internal fun FiatCurrency.toEntity() = FiatCurrencyEntity(
    dbId = null,
    id = id,
    name = name,
    symbol = symbol,
    code = code,
)
