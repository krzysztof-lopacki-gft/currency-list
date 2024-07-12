package com.crypto.recruitmenttest.currencies.domain.repository

import com.crypto.recruitmenttest.currencies.domain.model.CryptoCurrency
import com.crypto.recruitmenttest.currencies.domain.model.Currency
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType.Any
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType.Crypto
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType.Fiat
import com.crypto.recruitmenttest.currencies.domain.model.FiatCurrency
import com.crypto.recruitmenttest.currencies.domain.services.CurrenciesFetcher
import com.crypto.recruitmenttest.currencies.domain.storage.CurrenciesStorage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID

private val CRYPTO_CURRENCIES_LIST = listOf(cryptoCurrency(), cryptoCurrency(), cryptoCurrency())
private val FIAT_CURRENCIES_LIST = listOf(fiatCurrency(), fiatCurrency(), fiatCurrency())

class CurrenciesRepositoryTest {
    private val currenciesStorageMock = mockk<CurrenciesStorage>()
    private val currenciesFetcherMock = mockk<CurrenciesFetcher>()

    private lateinit var repository: CurrenciesRepository

    @Before
    fun setUp() {
        repository = CurrenciesRepository(currenciesStorageMock, currenciesFetcherMock)
    }

    @After
    fun cleanup() {
        unmockkAll()
    }

    @Test
    fun `given local storage is empty when getCurrencies is invoked then return empty list `() = runTest {
        // given
        coEvery { currenciesStorageMock.getCurrencies(Crypto) } returns emptyList()
        coEvery { currenciesStorageMock.getCurrencies(Fiat) } returns emptyList()
        coEvery { currenciesStorageMock.getCurrencies(Any) } returns emptyList()

        // when
        val result1 = repository.getCurrencies(Crypto)
        val result2 = repository.getCurrencies(Fiat)
        val result3 = repository.getCurrencies(Any)

        // then
        assert(result1.isEmpty())
        assert(result2.isEmpty())
        assert(result3.isEmpty())
    }

    @Test
    fun `given local storage is populated when getCurrencies is invoked then return corresponding data`() = runTest {
        // given
        coEvery { currenciesStorageMock.getCurrencies(Crypto) } returns CRYPTO_CURRENCIES_LIST
        coEvery { currenciesStorageMock.getCurrencies(Fiat) } returns FIAT_CURRENCIES_LIST
        coEvery { currenciesStorageMock.getCurrencies(Any) } returns CRYPTO_CURRENCIES_LIST + FIAT_CURRENCIES_LIST

        // when
        val result1 = repository.getCurrencies(Crypto)
        val result2 = repository.getCurrencies(Fiat)
        val result3 = repository.getCurrencies(Any)

        // then
        assertThat(result1, hasItems(*CRYPTO_CURRENCIES_LIST.toTypedArray()))
        assertEquals(result1.size, CRYPTO_CURRENCIES_LIST.size)
        assertThat(result2, hasItems(*FIAT_CURRENCIES_LIST.toTypedArray()))
        assertEquals(result2.size, FIAT_CURRENCIES_LIST.size)
        assertThat(result3, hasItems(*(CRYPTO_CURRENCIES_LIST + FIAT_CURRENCIES_LIST).toTypedArray()))
        assertEquals(result3.size, FIAT_CURRENCIES_LIST.size + CRYPTO_CURRENCIES_LIST.size)
    }

    @Test
    fun `when clear is invoked then clear local storage`() = runTest {
        // give
        coEvery { currenciesStorageMock.clear() } returns Unit

        // when
        repository.clear()

        // then
        coVerify { currenciesStorageMock.clear() }
    }

    @Test
    fun `when getCurrencies is invoked after local storage is cleared then return empty list `() = runTest {
        // given
        coEvery { currenciesStorageMock.getCurrencies(Crypto) } returns CRYPTO_CURRENCIES_LIST
        coEvery { currenciesStorageMock.getCurrencies(Fiat) } returns FIAT_CURRENCIES_LIST
        coEvery { currenciesStorageMock.getCurrencies(Any) } returns CRYPTO_CURRENCIES_LIST + FIAT_CURRENCIES_LIST
        coEvery { currenciesStorageMock.clear() } answers {
            coEvery { currenciesStorageMock.getCurrencies(Crypto) } returns emptyList()
            coEvery { currenciesStorageMock.getCurrencies(Fiat) } returns emptyList()
            coEvery { currenciesStorageMock.getCurrencies(Any) } returns emptyList()
        }

        // when
        repository.clear()
        val result1 = repository.getCurrencies(Crypto)
        val result2 = repository.getCurrencies(Fiat)
        val result3 = repository.getCurrencies(Any)

        // then
        assert(result1.isEmpty())
        assert(result2.isEmpty())
        assert(result3.isEmpty())
    }

    @Test
    fun `given local storage is empty when loadData is invoked then load data using fetcher and save it to the local storage`() = runTest {
        // given
        coEvery { currenciesFetcherMock.fetchCurrencies(Crypto) } returns CRYPTO_CURRENCIES_LIST
        coEvery { currenciesFetcherMock.fetchCurrencies(Fiat) } returns FIAT_CURRENCIES_LIST
        coEvery { currenciesStorageMock.containsAnyData() } returns false
        coEvery { currenciesStorageMock.saveCurrencies(any<List<Currency>>()) } returns Unit

        // when
        repository.loadData()

        coVerify { currenciesStorageMock.saveCurrencies(CRYPTO_CURRENCIES_LIST) }
        coVerify { currenciesStorageMock.saveCurrencies(FIAT_CURRENCIES_LIST) }
    }

    @Test
    fun `given local storage is populated when loadData is invoked do nothing`() = runTest {
        coEvery { currenciesStorageMock.containsAnyData() } returns true
        coEvery { currenciesStorageMock.saveCurrencies(any<List<Currency>>()) } returns Unit

        // when
        repository.loadData()

        // then
        coVerify(exactly = 0) { currenciesStorageMock.saveCurrencies(any<List<Currency>>()) }
    }
}

private fun cryptoCurrency() = CryptoCurrency(
    id = UUID.randomUUID().toString(),
    name = UUID.randomUUID().toString(),
    symbol = UUID.randomUUID().toString()
)

private fun fiatCurrency() = FiatCurrency(
    id = UUID.randomUUID().toString(),
    name = UUID.randomUUID().toString(),
    symbol = UUID.randomUUID().toString(),
    code = UUID.randomUUID().toString()
)
