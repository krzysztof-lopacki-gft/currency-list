package com.crypto.recruitmenttest.currencylist.ui.screens.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.crypto.recruitmenttest.common.ui.binding.viewBinding
import com.crypto.recruitmenttest.common.ui.fragment.handleBackButton
import com.crypto.recruitmenttest.common.ui.mvi.MviViewModel
import com.crypto.recruitmenttest.common.ui.mvi.ViewEffect
import com.crypto.recruitmenttest.common.ui.mvi.handleNavigationEffects
import com.crypto.recruitmenttest.common.ui.mvi.observeViewState
import com.crypto.recruitmenttest.currencylist.ui.R
import com.crypto.recruitmenttest.currencylist.ui.databinding.FragmentCurrencyListBinding
import com.crypto.recruitmenttest.currencylist.ui.model.CurrencyInfo
import com.crypto.recruitmenttest.currencylist.ui.screens.list.CurrencyListNavigationEffect.NavigateBack
import com.crypto.recruitmenttest.currencylist.ui.screens.list.CurrencyListViewEvent.OnBackClicked
import com.crypto.recruitmenttest.currencylist.ui.screens.list.CurrencyListViewEvent.OnDataSourceProvided
import com.crypto.recruitmenttest.currencylist.ui.screens.list.CurrencyListViewEvent.OnSearchQueryUpdated
import com.crypto.recruitmenttest.currencylist.ui.screens.list.adapter.CurrencyListAdapter
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class CurrencyListFragment : Fragment(R.layout.fragment_currency_list), CurrencyListView {
    private val viewModel: MviViewModel<CurrencyListViewState, CurrencyListViewEvent, CurrencyListNavigationEffect, ViewEffect> by viewModel<CurrencyListViewModel>()
    private val binding by viewBinding(FragmentCurrencyListBinding::bind)
    private val currenciesListAdapter = CurrencyListAdapter()

    override fun setDataSource(data: Flow<List<CurrencyInfo>?>) {
        viewModel.onEvent(OnDataSourceProvided(data))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupBackButton()
        observeViewState()
        handleNavigationEffects()
    }

    private fun setupToolbar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)
            addMenuProvider(CurrencyListMenuProvider(OnQueryTextListener()), viewLifecycleOwner)
            binding.toolbar.setNavigationOnClickListener { viewModel.onEvent(OnBackClicked) }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = currenciesListAdapter
    }

    private fun setupBackButton() {
        handleBackButton { viewModel.onEvent(OnBackClicked) }
    }

    /**
     * [INFO FOR REVIEWER]
     * Why do we prefer passing values to views using pure Kotlin instead of Data Binding?
     * Although data binding is a popular solution used to pass values from the view model to the view, it often offers few benefits and has many drawbacks:
     * - in the MVI architecture, code written in Fragments is typically shorter than data binding equivalent written in XML (binding + imports),
     * - IDE support for adding imports and syntax suggestions for data binding in XML files is very limited,
     * - refactoring (moving models, renaming properties) usually results in compilation errors,
     * - depending on the project size, compilation time can increase by several tens of percent after adding data binding.
     */
    private fun observeViewState() {
        observeViewState(viewModel) { state ->
            currenciesListAdapter.submitList(state.currencies)
            binding.loadingIndicator.isVisible = state.isLoadingIndicatorVisible
            binding.noDataInfo.isVisible = state.isNoDataInfoVisible
            binding.noResultsInfo.isVisible = state.isNoResultsInfoVisible
        }
    }

    private fun handleNavigationEffects() = handleNavigationEffects(viewModel) { navigationEffect ->
        when (navigationEffect) {
            NavigateBack -> findNavController().popBackStack()
        }
    }

    private inner class OnQueryTextListener : SearchView.OnQueryTextListener {
        init {
            viewModel.onEvent(OnSearchQueryUpdated(""))
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            viewModel.onEvent(OnSearchQueryUpdated(query ?: ""))
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            viewModel.onEvent(OnSearchQueryUpdated(newText ?: ""))
            return true
        }
    }

    private class CurrencyListMenuProvider(
        private val onQueryTextListener: SearchView.OnQueryTextListener,
    ) : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.currency_list_menu, menu)
            with(menu.findItem(R.id.action_search).actionView as SearchView) {
                queryHint = resources.getString(R.string.currency_list_search_hint)
                setOnQueryTextListener(onQueryTextListener)
            }
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
    }
}

