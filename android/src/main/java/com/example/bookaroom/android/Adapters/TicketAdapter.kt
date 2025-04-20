package com.example.bookaroom.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.Room
import com.example.bookaroom.Objects.Ticket
import com.example.bookaroom.R

/**
 * Adapter utilizado para cargar los tickets del inventario.
 */
class TicketAdapter(private val reservas: List<Ticket>, private val events : List<Event>, private val context: Context, private val onItemClick: (Ticket) -> Unit) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ticketName: TextView = itemView.findViewById(R.id.TicketTitle)
        val ticketRoom: TextView = itemView.findViewById(R.id.TicketRoom)
        val ticketDate: TextView = itemView.findViewById(R.id.TicketDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_inventory_format, parent, false)
        return TicketViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val reserva = reservas[position]
            val event = events.find { it.id_esdeveniment == reserva.getIdEvent() }
            holder.ticketName.text = event?.getTitle()
            holder.ticketRoom.text = "SALA" + event?.getIdSala().toString()
            holder.ticketDate.text = event?.getDataInici().toString()
            holder.itemView.setOnClickListener {
                onItemClick(reserva)
            }
    }


    override fun getItemCount(): Int = reservas.size
}
