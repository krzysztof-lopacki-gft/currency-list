package com.crypto.recruitmenttest.currencies.data.storage.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crypto.recruitmenttest.currencies.domain.model.CryptoCurrency

@Entity(tableName = "crypto_currencies")
class CryptoCurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val dbId: Long?,
    val id: String,
    val name: String,
    val symbol: String,
) {
    fun toDomain() = CryptoCurrency(
        id = id,
        name = name,
        symbol = symbol
    )
}

internal fun CryptoCurrency.toEntity() = CryptoCurrencyEntity(
    dbId = null,
    id = id,
    name = name,
    symbol = symbol,
)
