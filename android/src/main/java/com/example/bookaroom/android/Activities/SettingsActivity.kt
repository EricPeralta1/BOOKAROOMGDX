package com.example.bookaroom.android.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var user : User

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        user = intent.getParcelableExtra<User>("user")!!

        val username = findViewById<TextView>(R.id.UserNameText)
        username.text = user.getName() + " " + user.getSurname()

        activateNavBar()
        activateOptions()
    }

    /**
     * Activa las opciones del menú.
     */
    private fun activateOptions() {
        val editUser = findViewById<ConstraintLayout>(R.id.editUserButton)
        val changeLanguage = findViewById<ConstraintLayout>(R.id.changeLangButton)
        val exit = findViewById<ConstraintLayout>(R.id.exitButton)

        editUser.setOnClickListener {
            val intent = Intent(this, EditUserActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
        }

        changeLanguage.setOnClickListener {
            val intent = Intent(this, ChangeLanguageActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
        }

        exit.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    /**
     * Inicializa la barra de navegación inferior.
     */
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
