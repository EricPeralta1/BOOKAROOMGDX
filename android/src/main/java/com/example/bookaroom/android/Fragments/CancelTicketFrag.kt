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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookaroom.Adapters.TicketAdapter
import com.example.bookaroom.Objects.Ticket
import com.example.bookaroom.Objects.loadEventsFromJSON
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.Objects.loadTicketsFromJSON
import com.example.bookaroom.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

        initializeButton(selectedEvent)
    }

    /**
     * Permite cancelar la reserva. Comprueba que se hayan aceptado los términos antes de proceder.
     */
    private fun initializeButton(ticket: Ticket) {
        val acceptCancel = view?.findViewById<TextView>(R.id.cancelReservaButton)!!
        val backButton = view?.findViewById<TextView>(R.id.BackInventoryButton)!!
        val checkBoxCancel = view?.findViewById<CheckBox>(R.id.checkBoxCancel)!!


        acceptCancel.setOnClickListener {
            if (checkBoxCancel.isChecked){
                disableTicket(ticket)
                updateRecycler()
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
        val file = File(requireActivity().filesDir, "ticket.json")
        val tickets: MutableList<Ticket>
        if (file.exists()) {
            val inputStream = FileInputStream(file)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = bufferedReader.use { it.readText() }

            val gson = Gson()
            val projectObjects = object : TypeToken<List<Ticket>>() {}.type
            tickets = gson.fromJson(jsonText, projectObjects)

            val ticketToDisable = tickets.find { it.getId() == ticket.getId() }!!
            ticketToDisable.setId(0)

            val updatedJson = gson.toJson(tickets)
            try {
                val outputStream = FileOutputStream(file)
                val writer = OutputStreamWriter(outputStream)
                writer.write(updatedJson)
                writer.flush()
                writer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        } else {
            tickets = loadTicketsFromJSON(loadJsonFromRaw(requireActivity(), R.raw.tickets)!!)

            val ticketToDisable = tickets.find { it.getId() == ticket.getId() }!!
            ticketToDisable.setId(0)

            val gson = Gson()
            val chatJson = gson.toJson(tickets)
            val file = File(requireActivity().filesDir, "ticket.json")

            try {
                val outputStream = FileOutputStream(file)
                val writer = OutputStreamWriter(outputStream)
                writer.write(chatJson)
                writer.flush()
                writer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Actualiza el recyclerView para mostrar los tickets actuales.
     */
    private fun updateRecycler() {
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.recyclerView)!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        val eventList = (loadEventsFromJSON(loadJsonFromRaw(requireContext(), R.raw.events)!!))
        var tickets : ArrayList<Ticket> = ArrayList()

        val file = File(requireActivity().filesDir, "ticket.json")
        if (file.exists()) {
            val inputStream = FileInputStream(file)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = bufferedReader.use { it.readText() }

            val gson = Gson()
            val projectObjects = object : TypeToken<List<Ticket>>() {}.type
            tickets = gson.fromJson(jsonText, projectObjects)
        } else {
            tickets = loadTicketsFromJSON(loadJsonFromRaw(requireContext(), R.raw.tickets)!!)
        }
        val activatedTickets = chargeActivatedTickets(tickets)

        val ticketAdapter = TicketAdapter(activatedTickets,eventList, requireContext()) { event ->
            onTicketClick(event)
        }

        recyclerView.adapter = ticketAdapter
    }

    /**
     * Devuelve una lista con los tickets activos.
     */
    private fun chargeActivatedTickets(ticketList : ArrayList<Ticket>) : ArrayList<Ticket>{
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

