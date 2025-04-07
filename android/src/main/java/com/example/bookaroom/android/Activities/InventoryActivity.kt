package com.example.bookaroom.android.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bookaroom.Adapters.TicketAdapter
import com.example.bookaroom.Fragments.EmptyInventoryFrag
import com.example.bookaroom.Fragments.InventoryDetailsFrag
import com.example.bookaroom.Objects.Ticket
import com.example.bookaroom.Objects.User
import com.example.bookaroom.Objects.loadEventsFromJSON
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.Objects.loadTicketsFromJSON
import com.example.bookaroom.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class InventoryActivity : AppCompatActivity() {
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)


        user = intent.getParcelableExtra<User>("user")!!


        activateNavBar()
        initializeFragment()
        showCurrentReservas()

    }

    private fun showCurrentReservas() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        val eventList = (loadEventsFromJSON(loadJsonFromRaw(this, R.raw.events)!!))
        var tickets : ArrayList<Ticket> = ArrayList()

        val file = File(this.filesDir, "ticket.json")
        if (file.exists()) {
            val inputStream = FileInputStream(file)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = bufferedReader.use { it.readText() }

            val gson = Gson()
            val projectObjects = object : TypeToken<List<Ticket>>() {}.type
            tickets = gson.fromJson(jsonText, projectObjects)
        } else {
            tickets = loadTicketsFromJSON(loadJsonFromRaw(this, R.raw.tickets)!!)
        }
        val activatedTickets = chargeActivatedTickets(tickets)

        val ticketAdapter = TicketAdapter(activatedTickets,eventList, this) { event ->
            onTicketClick(event)
        }

        recyclerView.adapter = ticketAdapter
    }

    private fun chargeActivatedTickets(ticketList : ArrayList<Ticket>) : ArrayList<Ticket>{
        val activatedTickets : ArrayList<Ticket> = ArrayList()

        for (ticket in ticketList){
            if (ticket.getEstat() == 1){
                activatedTickets.add(ticket)
            }
        }

        return activatedTickets
    }

    private fun onTicketClick(ticket: Ticket) {
        val detailsTicketFrag = InventoryDetailsFrag()
        val bundle = Bundle()
        bundle.putParcelable("ticket", ticket)
        detailsTicketFrag.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.selectedReservaView, detailsTicketFrag)
            .addToBackStack(null)
            .commit()
    }

    private fun initializeFragment() {
        val noDetailsFrag = EmptyInventoryFrag()

        supportFragmentManager.beginTransaction()
            .replace(R.id.selectedReservaView, noDetailsFrag)
            .addToBackStack(null)
            .commit()
    }

    private fun activateNavBar() {
        val navSearch = findViewById<ImageView>(R.id.navSearchIcon)
        val navChat = findViewById<ImageView>(R.id.navChatIcon)
        val navInventori = findViewById<ImageView>(R.id.navInventariIcon)
        val navSettings = findViewById<ImageView>(R.id.navProfileIcon)


        if (user.getType() == "Event Organizer") {
            val navSearch = findViewById<ImageView>(R.id.navSearchIcon)
            navSearch.setBackgroundResource(R.drawable.event_ic)
        }

        navSearch.setOnClickListener {
            if (user.getType() == "Event Organizer"){
                val intent = Intent(this, CreateEventActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, ManualSearchActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            }
        }

        navChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
        }


        navInventori.setOnClickListener {
            val intent = Intent(this, InventoryActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
        }

        navSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
        }
    }
}
