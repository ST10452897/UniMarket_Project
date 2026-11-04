package za.co.unimarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import za.co.unimarket.MainActivity
import za.co.unimarket.R
import za.co.unimarket.UniMarketApi
import za.co.unimarket.adapters.NotificationAdapter
import za.co.unimarket.data.MockData

class NotificationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavVisible(true)

        view.findViewById<Button>(R.id.btnBackToMarketplace)?.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(HomeFragment())
        }

        val rvNotifications = view.findViewById<RecyclerView>(R.id.rvNotifications)
        rvNotifications?.layoutManager = LinearLayoutManager(context)
        val adapter = NotificationAdapter(MockData.notifications)
        rvNotifications?.adapter = adapter

        val localRepo = UniMarketApi.getLocalRepository(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            localRepo.getNotificationsFlow().collect { notifications ->
                if (notifications.isNotEmpty()) {
                    rvNotifications?.adapter = NotificationAdapter(notifications)
                }
            }
        }
    }
}
