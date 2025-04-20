package com.example.bookaroom.android.Activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookaroom.R

class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeSpinner()

        val createButton = findViewById<TextView>(R.id.createAccountButton)
        createButton.setOnClickListener {
            createAccount()
        }


    }

    private fun initializeSpinner() {
        val userTypes = listOf("Common User", "Event Organizer")
        val spinner = findViewById<Spinner>(R.id.userTypeSpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun createAccount() {
        val username = findViewById<EditText>(R.id.UsernameText).text.toString()
        val surname = findViewById<EditText>(R.id.SurnameText).text.toString()
        val email = findViewById<EditText>(R.id.emailText).text.toString()
        val password = findViewById<EditText>(R.id.PasswordTextRg).text.toString()
        val confirmPassword = findViewById<EditText>(R.id.passwordTextConfirmRg).text.toString()
        val userType = findViewById<Spinner>(R.id.userTypeSpinner).selectedItem.toString()


        if (confirmPassword != password) {
            Toast.makeText(this, getString(R.string.passwordNotSame), Toast.LENGTH_SHORT).show()
        } else {

        }
    }
}
