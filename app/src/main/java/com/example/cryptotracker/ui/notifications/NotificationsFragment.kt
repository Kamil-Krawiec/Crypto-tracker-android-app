// app/src/main/java/com/example/cryptotracker/ui/notifications/NotificationsFragment.kt
package com.example.cryptotracker.ui.notifications

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.R
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.repository.AlertRepository
import com.example.cryptotracker.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    private var _bind: FragmentNotificationsBinding? = null
    private val binding get() = _bind!!

    private val viewModel: NotificationsViewModel by viewModels {
        NotificationsViewModel.Factory(
            AlertRepository(
                CryptoDatabase.getInstance(requireContext()).alertDao()
            )
        )
    }

    private val adapter by lazy {
        AlertAdapter(
            onClick  = { alert -> viewModel.markSeen(alert) },
            onDelete = { alert -> viewModel.deleteAlert(alert) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _bind = FragmentNotificationsBinding.bind(view)

        binding.rvAlerts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NotificationsFragment.adapter
        }

        viewModel.alerts.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}