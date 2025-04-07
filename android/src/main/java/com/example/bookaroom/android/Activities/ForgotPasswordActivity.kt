package com.example.bookaroom.android.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookaroom.R

class ForgotPasswordActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgottenpassword)

        sendEmail()

    }

    private fun sendEmail() {
        TODO("Not yet implemented")
    }
}
