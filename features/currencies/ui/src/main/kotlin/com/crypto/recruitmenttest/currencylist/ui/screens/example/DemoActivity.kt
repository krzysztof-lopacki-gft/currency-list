package com.crypto.recruitmenttest.currencylist.ui.screens.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crypto.recruitmenttest.common.ui.mvi.MviViewModel
import com.crypto.recruitmenttest.common.ui.mvi.ViewEffect
import com.crypto.recruitmenttest.currencylist.ui.R
import com.crypto.recruitmenttest.currencylist.ui.screens.list.CurrencyListView
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoActivity : AppCompatActivity() {
    private val viewModel: MviViewModel<CurrenciesListDemoViewState, CurrenciesListDemoViewEvent, CurrenciesListDemoNavigationEffect, ViewEffect> by viewModel<CurrenciesListDemoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        supportFragmentManager.addFragmentOnAttachListener { _, fragment ->
            if (fragment is CurrencyListView) {
                fragment.setDataSource(viewModel.viewStates.map { state -> state.currenciesList })
            }
        }
    }
}
