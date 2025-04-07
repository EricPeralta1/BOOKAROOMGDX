package com.example.bookaroom.android.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R
import java.util.Locale

class ChangeLanguageActivity  : AppCompatActivity() {
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.language_activity)

        user = intent.getParcelableExtra<User>("user")!!

        activateNavBar()
        changeLanguage()
    }

    private fun changeLanguage() {
        val spanishButton = findViewById<RadioButton>(R.id.spainLang)
        val englishButton = findViewById<RadioButton>(R.id.englishLang)
        val catalanButton = findViewById<RadioButton>(R.id.catalanLang)

        spanishButton.setOnClickListener{
            englishButton.isChecked = false
            catalanButton.isChecked = false

            val locale = Locale("es")
            Locale.setDefault(locale)

            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            recreate()
        }

        englishButton.setOnClickListener{
            spanishButton.isChecked = false
            catalanButton.isChecked = false

            val locale = Locale("en")
            Locale.setDefault(locale)

            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            recreate()
        }

        catalanButton.setOnClickListener{
            englishButton.isChecked = false
            spanishButton.isChecked = false

            val locale = Locale("ca")
            Locale.setDefault(locale)

            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            recreate()
        }

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
