package za.co.unimarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.unimarket.R
import za.co.unimarket.model.NotificationItem

class NotificationAdapter(
    private val notifications: List<NotificationItem>,
    private val onItemClick: ((NotificationItem) -> Unit)? = null
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNotifTitle: TextView = itemView.findViewById(R.id.tvNotifTitle)
        val tvNotifDesc: TextView = itemView.findViewById(R.id.tvNotifDesc)
        val tvNotifTime: TextView = itemView.findViewById(R.id.tvNotifTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notif = notifications[position]
        holder.tvNotifTitle.text = notif.title
        holder.tvNotifDesc.text = notif.message
        holder.tvNotifTime.text = notif.timeAgo

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(notif)
        }
    }

    override fun getItemCount(): Int = notifications.size
}
