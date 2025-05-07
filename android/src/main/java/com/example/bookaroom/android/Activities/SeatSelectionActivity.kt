package com.example.bookaroom.android.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.Ticket
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository
import com.example.bookaroom.android.GDX.GameFragment
import kotlinx.coroutines.launch


class SeatSelectionActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks  {

    private lateinit var selectedSeatTextView: TextView
    private lateinit var user : User
    private lateinit var selectedEvent : Event
    private var reservedSeats = mutableListOf<Int>()
    private var currentSeatNumber : Int = 0


    /**
     * Inicia GameFragment, el metodo de LIBGDX que permite añadir codigo de LIBGDX a
     * una vista de la aplicación.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserva_butaca)

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        selectedSeatTextView = findViewById(R.id.selectedSeatText)
        user = intent.getParcelableExtra<User>("user")!!
        selectedEvent = intent?.getParcelableExtra<Event>("event")!!

        lifecycleScope.launch {
            val tickets = ApiRepository.getTicketsFromEvent(selectedEvent.event_id)!!
            for (ticket in tickets){
                reservedSeats.add(ticket.getId())
            }
            val libgdxFragment = GameFragment()
            libgdxFragment.setReservedSeats(reservedSeats)
            libgdxFragment.setSeatSelectionListener(object : GameFragment.SeatSelectionListener {
                @SuppressLint("SetTextI18n")
                override fun onSeatSelected(seatNumber: Int) {
                    selectedSeatTextView.text = getString(R.string.selectedButacaString) + "$seatNumber"
                    currentSeatNumber = seatNumber
                }
            })

            supportFragmentManager.beginTransaction()
                .add(R.id.libGDXView, libgdxFragment)
                .commit()

            val returnButton = findViewById<TextView>(R.id.cancelSeatReservation)
            returnButton.setOnClickListener {
                val intent = Intent(this@SeatSelectionActivity, SearchEventActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            }
        }

        val makeReserva = findViewById<TextView>(R.id.makeReservaButton)
        makeReserva.setOnClickListener {
            makeReservation()
        }

    }

    private fun makeReservation() {
        lifecycleScope.launch {
            val newTicket = Ticket(0, user.getIdUser(), selectedEvent.getIdEvent(), currentSeatNumber, 1)
            ApiRepository.createReserva(newTicket)
        }

        val intent = Intent(this, InventoryActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
        finish()
    }

    override fun exit() {
    }

    /**
     * Activa la barra de navegación inferior.
     */
    private fun activateNavBar() {
        val navSearch = findViewById<ImageView>(R.id.navSearchIcon)
        val navChat = findViewById<ImageView>(R.id.navChatIcon)
        val navMain = findViewById<ImageView>(R.id.navReservaIcon)
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

        navMain.setOnClickListener {
            val intent = Intent(this, SearchEventActivity::class.java)
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
