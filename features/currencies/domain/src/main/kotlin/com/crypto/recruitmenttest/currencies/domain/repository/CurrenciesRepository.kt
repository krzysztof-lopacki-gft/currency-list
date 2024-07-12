package com.crypto.recruitmenttest.currencies.domain.repository

import com.crypto.recruitmenttest.currencies.domain.model.Currency
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType.Crypto
import com.crypto.recruitmenttest.currencies.domain.model.CurrencyType.Fiat
import com.crypto.recruitmenttest.currencies.domain.services.CurrenciesFetcher
import com.crypto.recruitmenttest.currencies.domain.storage.CurrenciesStorage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CurrenciesRepository internal constructor(
    private val localCurrenciesStorage: CurrenciesStorage,
    private val remoteCurrenciesProvider: CurrenciesFetcher,
) {
    private val mutex: Mutex = Mutex()

    /**
     * [INFO FOR REVIEWER]
     * In "a classic repository" approach:
     * - the `getCurrencies` method should have a `forceRefresh: Boolean` argument
     * - if `forceRefresh` is set to `true` OR `localCurrenciesStorage` has not been populated with data so far, the `getCurrencies` method should:
     *      1) fetch the data using `remoteCurrenciesProvider`,
     *      2) save the fetched data to `localCurrenciesStorage`,
     *      3) return the data from `localCurrenciesStorage`.
     * - in other cases, the `getCurrencies` method should return the data from `localCurrenciesStorage`.
     *
     * However, the recruitment task has different requirements - local storage has to be entirely managed by buttons in DemoActivity.
     */
    suspend fun <T : Currency> getCurrencies(type: CurrencyType<out T>): List<T> = mutex.withLock {
        localCurrenciesStorage.getCurrencies(type)
    }

    /**
     * [INFO FOR REVIEWER]
     * In a "classic repository" approach, such a method is not required.
     * Rather, the `getCurrencies` method should have a `forceRefresh: Boolean` argument, which gives the possibility to skip the local cache/data source.
     */
    suspend fun clear() = mutex.withLock {
        localCurrenciesStorage.clear()
    }

    /**
     * [INFO FOR REVIEWER]
     * In a "classic repository" approach, such a method is not required. Repository data should be loaded on-demand whenever `getCurrencies` is called
     * and `localCurrenciesStorage` is not populated yet. Check the comments on the `getCurrencies` method for more details.
     */
    suspend fun loadData(): Boolean = mutex.withLock {
        if (!localCurrenciesStorage.containsAnyData()) {
            localCurrenciesStorage.saveCurrencies(remoteCurrenciesProvider.fetchCurrencies(Fiat))
            localCurrenciesStorage.saveCurrencies(remoteCurrenciesProvider.fetchCurrencies(Crypto))
            return true
        }
        return false
    }
}
