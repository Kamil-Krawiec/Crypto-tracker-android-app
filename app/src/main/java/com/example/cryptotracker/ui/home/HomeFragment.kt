package com.example.cryptotracker.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.R
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _bind: FragmentHomeBinding? = null
    private val binding get() = _bind!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModel.Factory(
            CryptoRepository(
                CryptoDatabase.getInstance(requireContext()).assetDao()
            )
        )
    }

    private val adapter by lazy {
        HomeAdapter { asset ->
            viewModel.deleteAsset(asset)
            Toast.makeText(context, "Deleted ${asset.symbol}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _bind = FragmentHomeBinding.bind(view)

        binding.rvAssets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }

        viewModel.assets.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        binding.btnAddAsset.setOnClickListener {
            val sym = binding.etSymbol.text.toString().trim().uppercase()
            val qty = binding.etQuantity.text.toString().toDoubleOrNull()
            val price = binding.etPurchasePrice.text.toString().toDoubleOrNull()
            if (sym.isEmpty() || qty == null || price == null) {
                Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addAsset(sym, qty, price)
                binding.etSymbol.text?.clear()
                binding.etQuantity.text?.clear()
                binding.etPurchasePrice.text?.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}