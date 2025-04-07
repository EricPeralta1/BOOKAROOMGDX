package com.example.bookaroom.android.Activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateEventActivity  : AppCompatActivity() {
    private lateinit var user : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        user = intent.getParcelableExtra<User>("user")!!
        activateNavBar()
        initializeCalendars()
        saveEvent()
    }

    private fun saveEvent() {
        val name : String = findViewById<EditText>(R.id.nameEditText).text.toString()
        val aforament : String = findViewById<EditText>(R.id.aforamentEditText).text.toString()
        val email : String = findViewById<EditText>(R.id.emailEditText).text.toString()
        val pass : String = findViewById<EditText>(R.id.endDateET).text.toString()
        val confirmPass : String = findViewById<EditText>(R.id.confirmPasswordEditText).text.toString()

        val editedUser = user
        if (!name.isEmpty()){
            editedUser.setNom(name)
        }
        if(!aforament.isEmpty()){
            editedUser.setCognom(aforament)
        }
        if (!email.isEmpty()){
            editedUser.setEmail(email)
        }
        if (!pass.isEmpty() && pass == confirmPass){
            editedUser.setPass(pass)
        }

        val gson = Gson()
        val userJSON = gson.toJson(user)
        userJSON.toString()
    }

    private fun initializeCalendars() {
        val startDate = findViewById<TextView>(R.id.startDateET)
        val endDate = findViewById<TextView>(R.id.endDateET)

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


        startDate.setOnClickListener {
            showDatePickerDialog(startDate, dateFormatter)
        }

        endDate.setOnClickListener {
            showDatePickerDialog(endDate, dateFormatter)
        }
    }

    private fun showDatePickerDialog(textView: TextView, dateFormatter: SimpleDateFormat) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                textView.text = dateFormatter.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
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
