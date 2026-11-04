package za.co.unimarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import za.co.unimarket.MainActivity
import za.co.unimarket.R
import za.co.unimarket.UniMarketApi
import za.co.unimarket.adapters.MessageAdapter
import za.co.unimarket.data.MockData

class MessagesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavVisible(true)

        view.findViewById<TextView>(R.id.btnComposeMessage)?.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val res = UniMarketApi.repository.sendMessage("seller_01", "p1", "Is this item still available?")
                res.onSuccess {
                    Toast.makeText(context, "Message sent to seller!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val rvMessagesList = view.findViewById<RecyclerView>(R.id.rvMessagesList)
        rvMessagesList?.layoutManager = LinearLayoutManager(context)
        rvMessagesList?.adapter = MessageAdapter(MockData.messages) { thread ->
            Toast.makeText(context, "Opening chat with ${thread.senderName}", Toast.LENGTH_SHORT).show()
        }

        val localRepo = UniMarketApi.getLocalRepository(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            localRepo.getMessagesFlow().collect { threads ->
                if (threads.isNotEmpty()) {
                    rvMessagesList?.adapter = MessageAdapter(threads) { thread ->
                        Toast.makeText(context, "Opening chat with ${thread.senderName}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
