package com.crypto.recruitmenttest.currencies.data.services

import android.content.Context
import androidx.annotation.RawRes
import com.crypto.recruitmenttest.currencies.data.model.CryptoCurrencyDto
import com.crypto.recruitmenttest.currencies.data.model.FiatCurrencyDto
import com.crypto.recruitmenttest.currencies.domain.model.CryptoCurrency
import com.crypto.recruitmenttest.currencies.domain.model.Currency
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType
import com.crypto.recruitmenttest.currencies.domain.model.FiatCurrency
import com.crypto.recruitmenttest.currencies.domain.services.CurrenciesFetcher
import com.crypto.recruitmenttest.currency.data.R
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.buffer
import okio.source

internal class FileBasedCurrenciesFetcher(
    private val applicationContext: Context,
    private val moshi: Moshi,
) : CurrenciesFetcher {

    @Suppress("UNCHECKED_CAST") // We are confident that types are matching as we use `when(type)`
    override suspend fun <T : Currency> fetchCurrencies(type: CurrencyType<out T>): List<T> = when (type) {
        CurrencyType.Crypto -> fetchCryptoCurrencies()
        CurrencyType.Fiat -> fetchFiatCurrencies()
        CurrencyType.Any -> fetchCryptoCurrencies() + fetchFiatCurrencies()
    } as List<T>

    private fun fetchCryptoCurrencies(): List<CryptoCurrency> = parseToList<CryptoCurrencyDto>(R.raw.crypto_currencies)
        .map { currencyDto -> currencyDto.toDomain() }

    private fun fetchFiatCurrencies(): List<FiatCurrency> = parseToList<FiatCurrencyDto>(R.raw.fiat_currencies)
        .map { currencyDto -> currencyDto.toDomain() }

    private inline fun <reified T> parseToList(@RawRes jsonResId: Int): List<T> = applicationContext.resources
        .openRawResource(jsonResId)
        .source().buffer()
        .use { bufferedSource ->
            moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
                .fromJson(bufferedSource) ?: emptyList()
        }
}
