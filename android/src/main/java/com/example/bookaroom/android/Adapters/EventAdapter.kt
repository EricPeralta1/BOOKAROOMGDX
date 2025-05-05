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

    fun hexStringToByteArray(hex: String): ByteArray {
        var cleanedHex = hex.replace("0x", "").replace("\\s".toRegex(), "")
        if (cleanedHex.length % 2 != 0) {
            cleanedHex = "0$cleanedHex"
        }

        val len = cleanedHex.length
        val data = ByteArray(len / 2)
        for (i in 0 until len step 2) {
            data[i / 2] = ((Character.digit(cleanedHex[i], 16) shl 4) + Character.digit(cleanedHex[i + 1], 16)).toByte()
        }
        return data
    }

    fun getBitmapFromHex(hex: String): Bitmap? {
        val cleanedHex = hex.replace("0x", "").replace("\\s".toRegex(), "")

        try {
            val decodedBytes = hexStringToByteArray(cleanedHex)

            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun getItemCount(): Int = eventList.size
}
