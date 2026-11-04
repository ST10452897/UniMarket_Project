package za.co.unimarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.unimarket.R
import za.co.unimarket.model.MessageThread

class MessageAdapter(
    private val threads: List<MessageThread>,
    private val onItemClick: ((MessageThread) -> Unit)? = null
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMsgAvatar: TextView = itemView.findViewById(R.id.tvMsgAvatar)
        val tvMsgSender: TextView = itemView.findViewById(R.id.tvMsgSender)
        val tvMsgPreview: TextView = itemView.findViewById(R.id.tvMsgPreview)
        val tvMsgTime: TextView = itemView.findViewById(R.id.tvMsgTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_thread, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val thread = threads[position]
        holder.tvMsgAvatar.text = thread.senderInitials
        holder.tvMsgSender.text = thread.senderName
        holder.tvMsgPreview.text = thread.lastMessage
        holder.tvMsgTime.text = thread.timestamp

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(thread)
        }
    }

    override fun getItemCount(): Int = threads.size
}
