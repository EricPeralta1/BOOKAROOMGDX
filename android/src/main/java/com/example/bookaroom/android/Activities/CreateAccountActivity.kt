package com.example.bookaroom.android.Activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository
import com.example.bookaroom.android.API.ApiRepository.getUsers
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

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


        lifecycleScope.launch {
            try {
                val username = findViewById<EditText>(R.id.UsernameText).text.toString()
                val surname = findViewById<EditText>(R.id.SurnameText).text.toString()
                val email = findViewById<EditText>(R.id.emailText).text.toString()
                val password = findViewById<EditText>(R.id.PasswordTextRg).text.toString()
                val confirmPassword = findViewById<EditText>(R.id.passwordTextConfirmRg).text.toString()
                val hashedPassword = hashPassword(confirmPassword)
                val userType = findViewById<Spinner>(R.id.userTypeSpinner).selectedItem.toString()


                if (confirmPassword != password) {
                    Toast.makeText(this@CreateAccountActivity, getString(R.string.passwordNotSame), Toast.LENGTH_SHORT).show()
                } else {
                    val users = getUsers()
                    val userList = users?.toMutableList() as ArrayList<User>

                    val user = User(0, username, surname, email, hashedPassword, userType, 1)

                    val createdUser = ApiRepository.createUser(user)
                    if (createdUser != null) {
                        Toast.makeText(this@CreateAccountActivity, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CreateAccountActivity, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                    }
                }


            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun hashPassword(plainPassword: String): String {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt())
    }
}
