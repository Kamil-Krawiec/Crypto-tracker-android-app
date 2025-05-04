package com.example.cryptotracker.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.databinding.ItemAssetBinding

class HomeAdapter(
    private val onDelete: (CryptoAsset) -> Unit
) : ListAdapter<CryptoAsset, HomeAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemAssetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onDelete
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ViewHolder(
        private val binding: ItemAssetBinding,
        private val onDelete: (CryptoAsset) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asset: CryptoAsset) = with(binding) {
            tvAssetSymbol.text = asset.symbol
            tvAssetDetails.text =
                "Qty: ${asset.quantity} @ ${"%.2f".format(asset.purchasePrice)}"
            ivDelete.setOnClickListener { onDelete(asset) }
        }
    }

    class Diff : DiffUtil.ItemCallback<CryptoAsset>() {
        override fun areItemsTheSame(old: CryptoAsset, new: CryptoAsset) =
            old.id == new.id

        override fun areContentsTheSame(old: CryptoAsset, new: CryptoAsset) =
            old == new
    }
}