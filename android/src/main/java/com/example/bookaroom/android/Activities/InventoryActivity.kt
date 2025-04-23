package com.example.bookaroom.android.Activities

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
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
    internal var x1: Float = 0.toFloat()
    internal var x2: Float = 0.toFloat()
    internal var y1: Float = 0.toFloat()
    internal var y2: Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        user = intent.getParcelableExtra<User>("user")!!

        activateNavBar()
        initializeFragment()
        showCurrentReservas()

    }

    override fun onTouchEvent(tochevent: MotionEvent): Boolean {
        when (tochevent.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = tochevent.x
                y1 = tochevent.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = tochevent.x
                y2 = tochevent.y

                val MIN_DISTANCE = 150

                if (x2 - x1 > MIN_DISTANCE) {
                    val i = Intent(this, SettingsActivity::class.java)
                    i.putExtra("user", user)
                    startActivity(i)
                }

                if (x2 - x1 < MIN_DISTANCE) {
                    val i = Intent(this, SearchEventActivity::class.java)
                    i.putExtra("user", user)
                    startActivity(i)
                }
            }
        }
        return false
    }

    /**
     * Muestra la lista de reservas que el usuario posea.
     */
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

    /**
     * Permite cargar solo aquellas reservas que esten activas.
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
     * Al hacer clic en una reserva, se abre un fragment el cual muestra sus detalles.
     */
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

    /**
     * Por defecto, el fragment mostrarà un mensaje indicando que el usuario
     * debe elegir una reserva para ver sus detalles.
     */
    private fun initializeFragment() {
        val noDetailsFrag = EmptyInventoryFrag()

        supportFragmentManager.beginTransaction()
            .replace(R.id.selectedReservaView, noDetailsFrag)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Activa la barra de navegación inferior.
     */
    private fun activateNavBar() {
        val navSearch = findViewById<ImageView>(R.id.navSearchIcon)
        val navChat = findViewById<ImageView>(R.id.navChatIcon)
        val navMain = findViewById<ImageView>(R.id.navReservaIcon)
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

        navMain.setOnClickListener {
            val intent = Intent(this, SearchEventActivity::class.java)
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
