package com.example.bookaroom.android.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R

class EditUserActivity  : AppCompatActivity() {
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_user_activity)
        user = intent.getParcelableExtra<User>("user")!!

        val saveChanges = findViewById<TextView>(R.id.saveChanges)
        saveChanges.setOnClickListener {
            saveEdition()
        }
        activateNavBar()

    }

    private fun saveEdition() {
        val name: String = findViewById<EditText>(R.id.nameEditText).text.toString()
        val surname: String = findViewById<EditText>(R.id.aforamentEditText).text.toString()
        val startDate: String = findViewById<EditText>(R.id.startDateET).text.toString()
        val endDate: String = findViewById<EditText>(R.id.endDateET).text.toString()
        val sala: String = findViewById<EditText>(R.id.salaET).text.toString()
        val preu: Int = findViewById<EditText>(R.id.priceET).text.toString().toInt()


    }

    private fun activateNavBar() {
        val navSearch = findViewById<ImageView>(R.id.navSearchIcon)
        val navChat = findViewById<ImageView>(R.id.navChatIcon)
        val navMain = findViewById<ImageView>(R.id.navReservaIcon)
        val navInventori = findViewById<ImageView>(R.id.navInventariIcon)
        val navSettings = findViewById<ImageView>(R.id.navProfileIcon)

        navSearch.setOnClickListener {
            val intent = Intent(this, ManualSearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        navChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }

        navMain.setOnClickListener {
            val intent = Intent(this, SearchEventActivity::class.java)
            startActivity(intent)
            finish()
        }

        navInventori.setOnClickListener {
            val intent = Intent(this, InventoryActivity::class.java)
            startActivity(intent)
            finish()
        }

        navSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
