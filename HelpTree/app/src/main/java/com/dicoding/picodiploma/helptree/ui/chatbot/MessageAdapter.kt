package com.dicoding.picodiploma.helptree.ui.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.helptree.data.Message
import com.dicoding.picodiploma.helptree.databinding.MessageItemsBinding
import com.dicoding.picodiploma.helptree.utils.Constants.RECEIVE_ID
import com.dicoding.picodiploma.helptree.utils.Constants.SEND_ID

class MessageAdapter: RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    var messageList = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemMessage = MessageItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(itemMessage)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]
        when(currentMessage.id){
            SEND_ID -> {
                holder.binding.tvMessage.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.binding.tvBotMessage.visibility = View.GONE
            }
            RECEIVE_ID -> {
                holder.binding.tvBotMessage.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.binding.tvMessage.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun insertMessage(message: Message){
        this.messageList.add(message)
        notifyItemInserted(messageList.size)
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(val binding: MessageItemsBinding): RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                messageList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}