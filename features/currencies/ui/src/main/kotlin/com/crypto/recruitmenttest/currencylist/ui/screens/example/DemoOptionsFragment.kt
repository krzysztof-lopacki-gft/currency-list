package com.crypto.recruitmenttest.currencylist.ui.screens.example

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.crypto.recruitmenttest.common.ui.binding.viewBinding
import com.crypto.recruitmenttest.common.ui.mvi.MviViewModel
import com.crypto.recruitmenttest.common.ui.mvi.ViewEffect
import com.crypto.recruitmenttest.common.ui.mvi.handleNavigationEffects
import com.crypto.recruitmenttest.common.ui.mvi.handleViewEffects
import com.crypto.recruitmenttest.currencylist.ui.R
import com.crypto.recruitmenttest.currencylist.ui.databinding.FragmentDemoOptionsBinding
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoNavigationEffect.NavigateToCurrenciesList
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEffect.ShowToast
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEvent.OnClearDataClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEvent.OnLoadDataClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEvent.OnShowAllCurrenciesClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEvent.OnShowCryptoCurrenciesClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.example.CurrenciesListDemoViewEvent.OnShowFiatCurrenciesClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.example.DemoOptionsFragmentDirections.Companion.toCurrencyList
import org.koin.androidx.viewmodel.ext.android.activityViewModel

internal class DemoOptionsFragment : Fragment(R.layout.fragment_demo_options) {
    private val viewModel: MviViewModel<CurrenciesListDemoViewState, CurrenciesListDemoViewEvent, CurrenciesListDemoNavigationEffect, CurrenciesListDemoViewEffect> by activityViewModel<CurrenciesListDemoViewModel>()
    private val binding by viewBinding(FragmentDemoOptionsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewEvents()
        handleNavigationEffects()
        handleViewEffects()
    }

    private fun handleNavigationEffects() = handleNavigationEffects(viewModel) { effect ->
        when (effect) {
            NavigateToCurrenciesList -> findNavController().navigate(toCurrencyList())
        }
    }

    private fun handleViewEffects() = handleViewEffects(viewModel) { effect ->
        when(effect) {
            is ShowToast -> Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewEvents() {
        binding.demoOptionsClearData.setOnClickListener { viewModel.onEvent(OnClearDataClicked) }
        binding.demoOptionsLoadData.setOnClickListener { viewModel.onEvent(OnLoadDataClicked) }
        binding.demoOptionsShowCryptoCurrencies.setOnClickListener { viewModel.onEvent(OnShowCryptoCurrenciesClicked) }
        binding.demoOptionsShowFiatCurrencies.setOnClickListener { viewModel.onEvent(OnShowFiatCurrenciesClicked) }
        binding.demoOptionsShowAllCurrencies.setOnClickListener { viewModel.onEvent(OnShowAllCurrenciesClicked) }
    }
}
