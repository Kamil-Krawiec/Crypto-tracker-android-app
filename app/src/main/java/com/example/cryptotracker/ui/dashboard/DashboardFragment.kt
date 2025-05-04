// app/src/main/java/com/example/cryptotracker/ui/dashboard/DashboardFragment.kt
package com.example.cryptotracker.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.R
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels {
        DashboardViewModel.Factory(
            CryptoRepository(
                CryptoDatabase
                    .getInstance(requireContext())
                    .assetDao()
            )
        )
    }

    private val adapter = DashboardAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDashboardBinding.bind(view)

        // Setup RecyclerView
        binding.rvDashboard.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@DashboardFragment.adapter
        }

        // Observe and submit list
        viewModel.portfolioUiState.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}