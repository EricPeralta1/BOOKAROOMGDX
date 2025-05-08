package com.example.bookaroom.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.Ticket
import com.example.bookaroom.R
import java.text.SimpleDateFormat
import java.util.Locale

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
        val dateFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())
        val event = events.find { it.event_id == reserva.getIdEvent() }

            holder.ticketName.text = event?.getTitle()
            holder.ticketRoom.text = "SALA " + event?.getIdSala().toString() + " | BUTACA" + reserva.getSeatId()
            holder.ticketDate.text = event?.getDataInici()?.let { dateFormat.format(it) }
            holder.itemView.setOnClickListener {
                onItemClick(reserva)
            }
    }


    override fun getItemCount(): Int = reservas.size
}
