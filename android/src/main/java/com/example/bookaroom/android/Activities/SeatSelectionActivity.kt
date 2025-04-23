package com.example.bookaroom.android.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R
import com.example.bookaroom.android.GDX.GameFragment


class SeatSelectionActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks  {

    private lateinit var selectedSeatTextView: TextView
    private lateinit var user : User


    /**
     * Inicia GameFragment, el metodo de LIBGDX que permite añadir codigo de LIBGDX a
     * una vista de la aplicación.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserva_butaca)

        selectedSeatTextView = findViewById(R.id.selectedSeatText)
        user = intent.getParcelableExtra<User>("user")!!


        val libgdxFragment = GameFragment()
        libgdxFragment.setSeatSelectionListener(object : GameFragment.SeatSelectionListener {
            @SuppressLint("SetTextI18n")
            override fun onSeatSelected(seatNumber: Int) {
                selectedSeatTextView.text = getString(R.string.selectedButacaString) + "$seatNumber"
            }
        })

        supportFragmentManager.beginTransaction()
            .add(R.id.libGDXView, libgdxFragment)
            .commit()

        val returnButton = findViewById<TextView>(R.id.cancelSeatReservation)
        returnButton.setOnClickListener {
            val intent = Intent(this, SearchEventActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
        }
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
