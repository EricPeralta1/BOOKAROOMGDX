package com.example.bookaroom.android.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.Objects.loadUsersFromJSON

import com.example.bookaroom.R

class LoginActivity : AppCompatActivity() {

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

        forgotPasswordClick()
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
     * Al clicar en el botón de recuperar contraseña, se abre la actividad de recuperar contraseña.
     */
    private fun forgotPasswordClick() {
        val forgotPass = findViewById<TextView>(R.id.forgotButtonText)

        forgotPass.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
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
        val users = loadUsersFromJSON(loadJsonFromRaw(this, R.raw.users)!!)
        var found = false

            for (user in users){
                if (user.getEmail() == email.text.toString() && user.getPass() == password.text.toString()){
                    found = true
                    val intent = Intent(this, SearchEventActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                }
            }

        if (!found){
            Toast.makeText(this, getString(R.string.incorrectcredentials), Toast.LENGTH_SHORT).show()
        }
    }
}



