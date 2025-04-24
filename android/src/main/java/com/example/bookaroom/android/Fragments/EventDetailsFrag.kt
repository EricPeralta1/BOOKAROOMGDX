package com.example.bookaroom.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.bookaroom.R
import com.example.bookaroom.android.Activities.SeatSelectionActivity
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.Objects.loadUsersFromJSON
import java.text.SimpleDateFormat
import java.util.Locale


class EventDetailsFrag : Fragment() {

    private lateinit var user : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_info_reserva, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataBundle = arguments
        var selectedEvent = dataBundle?.getParcelable<Event>("event")!!
        var user = dataBundle.getParcelable<User>("user")!!

        val reservarButton : TextView? = view.findViewById(R.id.imageViewReserve)
        val cancelarReserva : TextView? = view.findViewById(R.id.imageViewCancel)

        reservarButton?.setOnClickListener{
            val intent = Intent(requireContext(), SeatSelectionActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            requireActivity().finish()
        }

        cancelarReserva?.setOnClickListener {
            val shadow = activity?.findViewById<View>(R.id.shadowBackrgound)
            val fragment = activity?.findViewById<FragmentContainerView>(R.id.showEventsDetails)

            shadow?.visibility = View.GONE
            fragment?.visibility = View.GONE
        }

        loadData(selectedEvent)
    }

    /**
     * Carga los datos del evento seleccionado.
     */
    private fun loadData(event : Event) {
        val eventTitle = view?.findViewById<TextView>(R.id.eventTitle)
        val userName = view?.findViewById<TextView>(R.id.eventUserName)
        val eventInfo = view?.findViewById<TextView>(R.id.eventInfo)
        val eventPrice = view?.findViewById<TextView>(R.id.eventPrice)
        val eventDate = view?.findViewById<TextView>(R.id.eventDate)
        val eventRoom = view?.findViewById<TextView>(R.id.eventRoom)
        val dateFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())
        val users = loadUsersFromJSON(loadJsonFromRaw(requireContext(), R.raw.users)!!)
        val ticketUser = users.find { it.getIdUser() == event.getIdUser() }!!


        eventTitle?.text =  event.getTitle()
        userName?.text = getString(R.string.createdBy) + ticketUser.getName()
        eventInfo?.text = event.getDecription()
        eventPrice?.text = getString(R.string.priceDetail) + event.getPrice() + " €"
        eventDate?.text = getString(R.string.dayDetail) + event.getDataInici()?.let { dateFormat.format(it) }
        eventRoom?.text = getString(R.string.roomDetail) + event.getIdSala()

    }
}
