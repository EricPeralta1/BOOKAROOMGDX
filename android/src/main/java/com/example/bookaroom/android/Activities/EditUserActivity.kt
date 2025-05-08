package com.example.bookaroom.android.Activities

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository
import com.example.bookaroom.android.API.ApiRepository.getUsers
import com.example.bookaroom.android.Activities.LoginActivity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

class EditUserActivity  : AppCompatActivity() {
    private lateinit var user : User
    internal var x1: Float = 0.toFloat()
    internal var x2: Float = 0.toFloat()
    internal var y1: Float = 0.toFloat()
    internal var y2: Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_user_activity)
        user = intent.getParcelableExtra<User>("user")!!

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val saveChanges = findViewById<TextView>(R.id.saveChanges)
        saveChanges.setOnClickListener {
            saveEdition()
        }
        activateNavBar()

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
                val swipeX = x2 - x1
                val swipeY = y2 - y1

                if (abs(swipeX) > MIN_DISTANCE && abs(swipeY) < MIN_DISTANCE) {
                    if (swipeX < 0) {
                        val i = Intent(this, InventoryActivity::class.java)
                        i.putExtra("user", user)
                        startActivity(i)
                        finish()
                    }
                }
            }
        }
        return false
    }

    /**
     * Permite guardar los cambios realizados a un usuario.
     */
    private fun saveEdition() {
        val name : String = findViewById<EditText>(R.id.nameEditText).text.toString()
        val surname : String = findViewById<EditText>(R.id.surnameEditText).text.toString()
        val email : String = findViewById<EditText>(R.id.emailEditText).text.toString()
        val pass : String = findViewById<EditText>(R.id.endDateET).text.toString()
        val confirmPass : String = findViewById<EditText>(R.id.confirmPasswordEditText).text.toString()
        var canEdit = true

        val editedUser = user
        user.setActive(1)

        if (!name.isEmpty()){
            editedUser.setNom(name)
        }
        if(!surname.isEmpty()){
            editedUser.setCognom(surname)
        }
        if (!email.isEmpty()){
            editedUser.setEmail(email)
        }
        if (!pass.isEmpty() && pass == confirmPass){
            if (pass.length < 8){
                Toast.makeText(applicationContext, "Password must be 8 characters or longer", Toast.LENGTH_SHORT).show()
                canEdit = false
            } else if (pass.contains("book")) {
                Toast.makeText(applicationContext, "There are prohibited characters on the password.", Toast.LENGTH_SHORT).show()
                canEdit = false
            } else {
                editedUser.setPass(pass)
            }
        }

        if (canEdit){
            lifecycleScope.launch {
                try {
                    val updateSuccessful = ApiRepository.updateUser(editedUser.getIdUser(), editedUser)
                    if (updateSuccessful) {
                        Toast.makeText(applicationContext, "User updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Failed to update user", Toast.LENGTH_SHORT).show()
                    }
                }catch (e: Exception)
                {
                    println("API Connexion Error")
                }
            }
        }
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

        navSearch.setOnClickListener {
            val intent = Intent(this, ManualSearchActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
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
