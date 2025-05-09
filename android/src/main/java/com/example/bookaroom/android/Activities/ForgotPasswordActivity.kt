package com.example.bookaroom.android.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository
import com.example.bookaroom.android.Activities.LoginActivity
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class ForgotPasswordActivity: AppCompatActivity() {

    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgottenpassword)

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)


        user = intent.getParcelableExtra<User>("user")!!

        val savePassword = findViewById<TextView>(R.id.PasswordText)
        savePassword.setOnClickListener {
            checkPassword()
        }
    }

    /**
     * Comprueba que la contraseña cumpla los requisitos. Si no cumple, no
     * se realiza el guardado.
     */
    private fun checkPassword(){
        lifecycleScope.launch {
            val password = findViewById<EditText>(R.id.PasswordText).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirmPasswordText).text.toString()

            if (password.length < 8){
                Toast.makeText(this@ForgotPasswordActivity, "Passwords must be 8 characters or longer.", Toast.LENGTH_SHORT).show()
            } else if (password.contains("book")){
                Toast.makeText(this@ForgotPasswordActivity, "There are prohibited characters on the password.", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword){
                Toast.makeText(this@ForgotPasswordActivity, getString(R.string.passwordNotSame), Toast.LENGTH_SHORT).show()
            } else {
                val hashedPassword = hashPassword(password)
                user.setPass(hashedPassword)
                ApiRepository.updateUser(user.getIdUser(), user)
                Toast.makeText(this@ForgotPasswordActivity, "Password updated succesfully. Welcome!", Toast.LENGTH_SHORT).show()
                returnMain()
            }
        }
    }

    /**
     * Envia al usuario a la actividad principal.
     */
    private fun returnMain(){
        val intent = Intent(this, SearchEventActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
        finish()
    }

    /**
     * Encripta la contaseña mediante BCrypt.
     */
    fun hashPassword(plainPassword: String): String {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt())
    }
}
