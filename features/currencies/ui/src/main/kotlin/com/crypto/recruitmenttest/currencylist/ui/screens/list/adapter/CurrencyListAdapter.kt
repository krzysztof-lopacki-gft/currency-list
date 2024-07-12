package com.crypto.recruitmenttest.currencylist.ui.screens.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crypto.recruitmenttest.currencylist.ui.R
import com.crypto.recruitmenttest.currencylist.ui.databinding.ItemCurrencyListBinding
import com.crypto.recruitmenttest.currencylist.ui.model.CurrencyInfo

internal class CurrencyListAdapter : ListAdapter<CurrencyInfo, CurrencyListAdapter.CurrencyListItemViewHolder>(CurrencyDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency_list, parent, false)
        return CurrencyListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    internal class CurrencyListItemViewHolder(
        private val itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemCurrencyListBinding = ItemCurrencyListBinding.bind(itemView)

        fun bind(currencyInfo: CurrencyInfo) {
            binding.currencyFirstLetter.text = currencyInfo.name.first().toString()
            binding.currencyName.text = currencyInfo.name
        }
    }

    internal class CurrencyDiffCallback : DiffUtil.ItemCallback<CurrencyInfo>() {
        override fun areItemsTheSame(oldItem: CurrencyInfo, newItem: CurrencyInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CurrencyInfo, newItem: CurrencyInfo): Boolean {
            return oldItem == newItem
        }
    }
}
