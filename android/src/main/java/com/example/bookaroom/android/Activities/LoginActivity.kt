package com.example.bookaroom.android.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookaroom.Adapters.EventAdapter
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.Objects.loadUsersFromJSON

import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository.getEvents
import com.example.bookaroom.android.API.ApiRepository.getUsers
import com.example.bookaroom.android.Activities.SearchEventActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private  var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val loginButton = findViewById<TextView>(R.id.loginButton)
        loginButton.setOnClickListener {
            checkLoginDetails()
        }

        makeAccountClick()
    }

    /**
     * Al clicar en el botón de crear cuenta, se abre la actividad de crear cuenta.
     */
    private fun makeAccountClick() {
        val createAccount = findViewById<TextView>(R.id.makeAccountBtn)
        createAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Comprueba que los datos introducidos por el usuario son correctos.
     * Si lo son, se pasa a la siguiente actividad, SearchEventActivity.
     */
    private fun checkLoginDetails() {
        val email = findViewById<TextView>(R.id.UsernameText)
        val password = findViewById<TextView>(R.id.PasswordText)
        var found = false

        lifecycleScope.launch {
            try {
                val users = getUsers()
                userList = users?.toMutableList() as ArrayList<User>

                for (user in userList){
                    if (user.getEmail() == email.text.toString() && user.getPass() == password.text.toString()){
                        if (password.text.contains("book")){
                            found = true
                            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            finish()
                        }else {
                            found = true
                            val intent = Intent(this@LoginActivity, SearchEventActivity::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            finish()
                        }
                    }
                }

                if (!found){
                    Toast.makeText(this@LoginActivity, getString(R.string.incorrectcredentials), Toast.LENGTH_SHORT).show()
                }

            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

    }
}



