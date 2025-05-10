package com.example.cryptotracker.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.databinding.ItemAlertBinding

class AlertAdapter(
    private val onClick: (PriceAlert) -> Unit,
    private val onDelete: (PriceAlert) -> Unit
) : ListAdapter<PriceAlert, AlertAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemAlertBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick,
            onDelete
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ViewHolder(
        private val binding: ItemAlertBinding,
        private val onClick: (PriceAlert) -> Unit,
        private val onDelete: (PriceAlert) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alert: PriceAlert) = with(binding) {
            tvAlertSymbol.text = alert.symbol
            val op = if (alert.isAboveThreshold) "≥" else "≤"
            tvAlertThreshold.text = "$op ${"%.2f".format(alert.targetPrice)}"

            root.setOnClickListener { onClick(alert) }
            ivAlertDelete.setOnClickListener { onDelete(alert) }

            // Gray out if already seen
            root.alpha = if (alert.seen) 0.5f else 1f
        }
    }

    class Diff : DiffUtil.ItemCallback<PriceAlert>() {
        override fun areItemsTheSame(old: PriceAlert, new: PriceAlert): Boolean =
            old.id == new.id

        override fun areContentsTheSame(old: PriceAlert, new: PriceAlert): Boolean =
            old == new
    }
}
