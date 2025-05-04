package com.example.cryptotracker.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.databinding.ItemDashboardBinding

class DashboardAdapter
    : ListAdapter<DashboardItem, DashboardAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDashboardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemDashboardBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardItem) = with(binding) {
            tvSymbol.text = item.symbol
            tvDetails.text = "Qty: ${item.quantity} @ $${"%.2f".format(item.purchasePrice)}"

            val pl = item.profitLoss
            val sign = if (pl >= 0) "+" else "-"
            tvPL.text = "$sign$${"%.2f".format(kotlin.math.abs(pl))}"
            val colorRes = if (pl >= 0)
                android.R.color.holo_green_dark else android.R.color.holo_red_dark
            tvPL.setTextColor(root.resources.getColor(colorRes, null))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DashboardItem>() {
        override fun areItemsTheSame(old: DashboardItem, new: DashboardItem) =
            old.symbol == new.symbol

        override fun areContentsTheSame(old: DashboardItem, new: DashboardItem) =
            old == new
    }
}