package com.example.bookaroom.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.R


/**
 * Adapter utilizado para cargar las listas de eventos.
 */
class EventAdapter(private val eventList: List<Event>, private val context: Context, private val onItemClick: (Event) -> Unit) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.eventTitle)
        val eventImage: ImageView = itemView.findViewById(R.id.eventImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.eventName.text = event.name
        val multimedia = event.getImageByte()

        Glide.with(holder.itemView.context)
            .load(multimedia)
            .into(holder.eventImage)

        holder.itemView.setOnClickListener {
            onItemClick(event)
        }
    }


    override fun getItemCount(): Int = eventList.size
}
