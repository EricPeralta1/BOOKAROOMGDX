package com.example.bookaroom.android.Activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.Objects.loadUsersFromJSON

import com.example.bookaroom.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<TextView>(R.id.loginButton)
        loginButton.setOnClickListener {
            checkLoginDetails()
        }
        forgotPasswordClick()
        makeAccountClick()
    }

    private fun makeAccountClick() {
        val makeAccount = findViewById<TextView>(R.id.makeAccountBtn)

        makeAccount.setOnClickListener {
            val intent = Intent(this, NewUserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun forgotPasswordClick() {
        val forgotPass = findViewById<TextView>(R.id.forgotButtonText)

        forgotPass.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkLoginDetails() {

        val email = findViewById<TextView>(R.id.UsernameText)
        val password = findViewById<TextView>(R.id.PasswordText)
        val users = loadUsersFromJSON(loadJsonFromRaw(this, R.raw.users)!!)

            for (user in users){
                if (user.getEmail() == email.text.toString() && user.getPass() == password.text.toString()){
                    val intent = Intent(this, SearchEventActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }



