package com.example.bookaroom.android.Activities

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookaroom.Adapters.EventAdapter
import com.example.bookaroom.Fragments.EventDetailsFrag
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import com.example.bookaroom.Objects.loadEventsFromJSON
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.R
import kotlin.math.abs

class SearchEventActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var user : User
    internal var x1: Float = 0.toFloat()
    internal var x2: Float = 0.toFloat()
    internal var y1: Float = 0.toFloat()
    internal var y2: Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        user = intent.getParcelableExtra<User>("user")!!

        activateNavBar()
        loadEvents()
    }

    /**
     * Carga los eventos en la lista.
     */
    private fun loadEvents() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val eventList = (loadEventsFromJSON(loadJsonFromRaw(this, R.raw.events)!!))

        val eventAdapter = EventAdapter(eventList, this) { event ->
            onEventClick(event)
        }

        recyclerView.adapter = eventAdapter
    }

    /**
     * Al hacer clic en un evento, se envia al fragment eventDetailsFrag, que muestra sus detalles.
     */
    private fun onEventClick(event: Event) {
        val eventDetailsFrag = EventDetailsFrag()
        val bundle = Bundle()
        bundle.putParcelable("event", event)
        bundle.putParcelable("user", user)
        eventDetailsFrag.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.showEventsDetails, eventDetailsFrag)
            .addToBackStack(null)
            .commit()

        val shadow = findViewById<View>(R.id.shadowBackrgound)
        val fragment = findViewById<FragmentContainerView>(R.id.showEventsDetails)

        shadow.visibility = View.VISIBLE
        fragment.visibility = View.VISIBLE
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
                    if (swipeX > 0) {
                        val i = Intent(this, InventoryActivity::class.java)
                        i.putExtra("user", user)
                        startActivity(i)
                        finish()
                    }
                }

                if (abs(swipeX) > MIN_DISTANCE && abs(swipeY) < MIN_DISTANCE) {
                    if (swipeX < 0) {
                        val i = Intent(this, ChatActivity::class.java)
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
     * Inicializa la barra de navegación.
     */
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
