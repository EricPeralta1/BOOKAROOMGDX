package com.example.bookaroom.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.Message
import com.example.bookaroom.Objects.User
import com.example.bookaroom.Objects.loadEventsFromJSON
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.R

/**
 * Adapter que permite cargar el formato del chat.
 */
class ChatAdapter(private val messageList: List<Message>, private val users : List<User>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatterName: TextView = itemView.findViewById(R.id.userNameMessage)
        val chatMessage: TextView = itemView.findViewById(R.id.messageContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_cell, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messageList[position]
        val user = users.find { it.getIdUser() == message.id_enviar }!!

        holder.chatterName.text = user.getName()
        holder.chatMessage.text = message.getMessage()

    }


    override fun getItemCount(): Int = messageList.size
}
