// app/src/main/java/com/example/cryptotracker/ui/notifications/AlertAdapter.kt
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
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
            val op = if (alert.above) "≥" else "≤"
            tvAlertThreshold.text = "$op ${"%.2f".format(alert.threshold)}"

            root.setOnClickListener { onClick(alert) }
            ivAlertDelete.setOnClickListener { onDelete(alert) }

            // gray out if already seen
            alpha = if (alert.seen) 0.5f else 1f
        }
    }

    class Diff : DiffUtil.ItemCallback<PriceAlert>() {
        override fun areItemsTheSame(old: PriceAlert, new: PriceAlert) =
            old.id == new.id

        override fun areContentsTheSame(old: PriceAlert, new: PriceAlert) =
            old == new
    }
}