package com.example.bookaroom.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookaroom.Adapters.TicketAdapter
import com.example.bookaroom.Objects.Ticket
import com.example.bookaroom.Objects.User
import com.example.bookaroom.Objects.loadEventsFromJSON
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.Objects.loadTicketsFromJSON
import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class CancelTicketFrag  : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View {
        val view = inflater.inflate(R.layout.fragment_cancel_ticket, container, false)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataBundle = arguments
        var selectedEvent = dataBundle?.getParcelable<Ticket>("ticket")!!
        var user = dataBundle?.getParcelable<User>("user")!!
        initializeButton(selectedEvent, user)
    }

    /**
     * Permite cancelar la reserva. Comprueba que se hayan aceptado los términos antes de proceder.
     */
    private fun initializeButton(ticket: Ticket, user: User) {
        val acceptCancel = view?.findViewById<TextView>(R.id.cancelReservaButton)!!
        val backButton = view?.findViewById<TextView>(R.id.BackInventoryButton)!!
        val checkBoxCancel = view?.findViewById<CheckBox>(R.id.checkBoxCancel)!!


        acceptCancel.setOnClickListener {
            if (checkBoxCancel.isChecked){
                disableTicket(ticket)
                updateRecycler(user)
                resetSelection()
                val fragmentView = activity?.findViewById<FragmentContainerView>(R.id.cancelTicketView)
                fragmentView?.visibility = View.GONE
                val shade = activity?.findViewById<View>(R.id.shadowBgInv)
                shade?.visibility = View.GONE

            } else {
                Toast.makeText(requireContext(), getString(R.string.checknotaccept), Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            val fragmentView = activity?.findViewById<FragmentContainerView>(R.id.cancelTicketView)
            fragmentView?.visibility = View.GONE
            val shade = activity?.findViewById<View>(R.id.shadowBgInv)
            shade?.visibility = View.GONE
        }
    }

    /**
     * Deshabilita el ticket seleccionado.
     */
    private fun disableTicket(ticket : Ticket) {
        lifecycleScope.launch {
            ticket.setStatus(0)
            ApiRepository.cancelEntrada(ticket.getId(), ticket)
        }
    }

    /**
     * Actualiza el recyclerView para mostrar los tickets actuales.
     */
    private fun updateRecycler(user: User) {
        lifecycleScope.launch {
            val recyclerView = activity?.findViewById<RecyclerView>(R.id.recyclerView)!!
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


            val eventList = ApiRepository.getEvents()!!
            var tickets = ApiRepository.getTicketsFromUser(user.getIdUser())!!

            val activatedTickets = chargeActivatedTickets(tickets)

            val ticketAdapter = TicketAdapter(activatedTickets,eventList, requireContext()) { event ->
                onTicketClick(event)
            }

            recyclerView.adapter = ticketAdapter
        }
    }

    /**
     * Devuelve una lista con los tickets activos.
     */
    private fun chargeActivatedTickets(ticketList : List<Ticket>) : List<Ticket>{
        val activatedTickets : ArrayList<Ticket> = ArrayList()

        for (ticket in ticketList){
            if (ticket.getEstat() == 1){
                activatedTickets.add(ticket)
            }
        }

        return activatedTickets
    }

    /**
     * Al hacer clic en un ticket, muestra sus detalles.
     */
    private fun onTicketClick(ticket: Ticket) {
        val detailsTicketFrag = InventoryDetailsFrag()
        val bundle = Bundle()
        bundle.putParcelable("ticket", ticket)
        detailsTicketFrag.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.selectedReservaView, detailsTicketFrag)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Reseta la vista del ticket seleccionado y muestra la pantalla por defecto.
     */
    private fun resetSelection() {
        val noDetailsFrag = EmptyInventoryFrag()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.selectedReservaView, noDetailsFrag)
            .addToBackStack(null)
            .commit()
    }
}

