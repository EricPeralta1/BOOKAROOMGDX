package com.example.bookaroom.android.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.example.bookaroom.R
import com.example.bookaroom.android.GDX.GameFragment


class SeatSelectionActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserva_butaca)

        val libgdxFragment = GameFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.libGDXView, libgdxFragment)
            .commit()
    }

    override fun exit() {
    }
}
