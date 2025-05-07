package com.example.bookaroom.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import com.example.bookaroom.Objects.Ticket
import com.example.bookaroom.Objects.User
import com.example.bookaroom.Objects.loadEventsFromJSON
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class InventoryDetailsFrag  : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View {
        val view = inflater.inflate(R.layout.fragment_inventory_content, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataBundle = arguments
        var selectedEvent = dataBundle?.getParcelable<Ticket>("ticket")!!
        var user = dataBundle.getParcelable<User>("user")!!

        chargeDetails(selectedEvent)
        cancelTicket(selectedEvent, user)
    }

    /**
     * Al hacer clic en cancelar ticket, muestra el fragment de cancelar ticket.
     */
    private fun cancelTicket(ticket: Ticket, user: User) {
        val cancelButton = view?.findViewById<TextView>(R.id.cancelTicketButton)

        cancelButton?.setOnClickListener {
            val cancelFrag = CancelTicketFrag()
            val bundle = Bundle()
            bundle.putParcelable("ticket", ticket)
            bundle.putParcelable("user", user)
            cancelFrag.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.cancelTicketView, cancelFrag)
                .addToBackStack(null)
                .commit()

            val cancelTicketView = activity?.findViewById<FragmentContainerView>(R.id.cancelTicketView)
            cancelTicketView?.visibility = View.VISIBLE

            val shadow = activity?.findViewById<View>(R.id.shadowBgInv)
            shadow?.visibility = View.VISIBLE
        }
    }

    /**
     * Muestra los detalles del ticket seleccionado en el recyclerView superior.
     */
    private fun chargeDetails(ticket: Ticket) {
        lifecycleScope.launch {
            val events = ApiRepository.getEvents()!!
            val event = events.find { it.event_id == ticket.getIdEvent() }

            val ticketTitle = view?.findViewById<TextView>(R.id.TitleSelectedTicket)
            val ticketDescription = view?.findViewById<TextView>(R.id.descriptionSelecTicket)
            val ticketStartDate = view?.findViewById<TextView>(R.id.dateStartSelectedTicket)
            val ticketEndDate = view?.findViewById<TextView>(R.id.saveChanges)
            val ticketPrice = view?.findViewById<TextView>(R.id.priceSelectedTicket)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            ticketTitle?.text = event?.getTitle()
            ticketDescription?.text = event?.getDecription()
            ticketStartDate?.text = event?.getDataInici()?.let { dateFormat.format(it) }
            ticketEndDate?.text = event?.getDataFinal()?.let { dateFormat.format(it) }
            ticketPrice?.text = event?.getPrice().toString()
        }
    }
}
