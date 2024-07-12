package com.crypto.recruitmenttest.currencylist.ui.screens.example

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.crypto.recruitmenttest.common.ui.mvi.MviViewModel
import com.crypto.recruitmenttest.common.ui.mvi.ViewEffect
import com.crypto.recruitmenttest.currencylist.ui.databinding.ActivityDemoBinding
import com.crypto.recruitmenttest.currencylist.ui.screens.list.CurrencyListView
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoActivity : AppCompatActivity() {
    private val viewModel: MviViewModel<CurrenciesListDemoViewState, CurrenciesListDemoViewEvent, CurrenciesListDemoNavigationEffect, ViewEffect> by viewModel<CurrenciesListDemoViewModel>()
    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        provideDataSourceToCurrencyListView()
    }

    private fun setupView() {
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun provideDataSourceToCurrencyListView() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navHostFragment.childFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(fm: FragmentManager, fragment: Fragment, v: View, savedInstanceState: Bundle?) {
                if (fragment is CurrencyListView) {
                    fragment.setDataSource(viewModel.viewStates.map { state -> state.currenciesList })
                }
            }
        }, false)
    }
}
