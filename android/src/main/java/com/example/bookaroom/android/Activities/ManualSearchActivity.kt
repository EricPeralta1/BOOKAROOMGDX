package com.example.bookaroom.android.Activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookaroom.Adapters.EventAdapter
import com.example.bookaroom.Fragments.EventDetailsFrag
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import com.example.bookaroom.Objects.loadEventsFromJSON
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository.getEvents
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class ManualSearchActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private  var eventList = ArrayList<Event>()

    private lateinit var user : User
    internal var x1: Float = 0.toFloat()
    internal var x2: Float = 0.toFloat()
    internal var y1: Float = 0.toFloat()
    internal var y2: Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_event)

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


        initializeSeekBar()
        initializeCalendars()

        val searchIcon = findViewById<ImageView>(R.id.SearchClick)

        searchIcon.setOnClickListener {
            val startDate = findViewById<TextView>(R.id.selectStartDate).text.toString()
            val endDate = findViewById<TextView>(R.id.SelectEndDate).text.toString()
            val eventName = findViewById<EditText>(R.id.searchEventBox).text.toString()
            val price = findViewById<TextView>(R.id.priceNumber).text.toString().toInt()

            updateEventList(startDate, endDate, eventName, price)
        }

        val reloadIc = findViewById<ImageView>(R.id.reloadList)

        reloadIc.setOnClickListener {
            loadEvents()
            val seekBar = findViewById<SeekBar>(R.id.seekBarSearch)
            seekBar.progress = 0

            val startDate = findViewById<TextView>(R.id.selectStartDate)
            val endDate = findViewById<TextView>(R.id.SelectEndDate)

            startDate.text = ""
            endDate.text = ""

        }
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

                if (abs(swipeX) > MIN_DISTANCE && Math.abs(swipeY) < MIN_DISTANCE) {
                    if (swipeX > 0) {
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
     * Inicializa los calendarios para seleccionar las fechas de inicio y fin del evento.
     */
    private fun initializeCalendars() {
        val startDate = findViewById<TextView>(R.id.selectStartDate)
        val endDate = findViewById<TextView>(R.id.SelectEndDate)

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


        startDate.setOnClickListener {
            showDatePickerDialog(startDate, dateFormatter)
        }

        endDate.setOnClickListener {
            showDatePickerDialog(endDate, dateFormatter)
        }
    }

    /**
     * Inicia SeekBar, una barra deslizante que permite seleccionar el precio según el valor
     * que tenga la barra.
     */
    private fun initializeSeekBar() {
        val seekBar = findViewById<SeekBar>(R.id.seekBarSearch)
        seekBar.max = 150
        seekBar.progress = 0

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val price = findViewById<TextView>(R.id.priceNumber)
                price.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


    }

    /**
     * Actualiza la lista de eventos según los filtros seleccionados por el usuario. Comprueba que
     * campos estan llenos o alterados y busca según esos datos.
     */
    private fun updateEventList(startDate : String, endDate : String, eventName: String, price: Int){
        lifecycleScope.launch {
            try {
                val events = getEvents()
                eventList = events?.toMutableList() as ArrayList<Event>
                var filteredEventList : ArrayList<Event> = ArrayList()

                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateFormatOriginal = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)

                eventList.forEach { event ->
                    var matches = true

                    if (eventName.isNotEmpty() && !event.getTitle()
                            .contains(eventName, ignoreCase = true)
                    ) {
                        matches = false
                    }

                    if (startDate.isNotEmpty()) {
                        try {
                            val filterStartDate = dateFormat.parse(startDate)

                            val eventStartDateOriginal =
                                dateFormatOriginal.parse(event.getDataInici().toString())!!

                            val eventStartDate = dateFormat.format(eventStartDateOriginal)

                            if (eventStartDateOriginal.before(filterStartDate)) {
                                matches = false
                            }
                        } catch (e: Exception) {
                            matches = false
                            e.printStackTrace()
                        }
                    }

                    if (endDate.isNotEmpty()) {
                        try {
                            val filterStartDate = dateFormat.parse(endDate)

                            val eventEndDateOriginal =
                                dateFormatOriginal.parse(event.getDataFinal().toString())!!

                            val eventStartDate = dateFormat.format(eventEndDateOriginal)

                            if (eventEndDateOriginal.after(filterStartDate)) {
                                matches = false
                            }
                        } catch (e: Exception) {
                            matches = false
                            e.printStackTrace()
                        }
                    }

                    if (price > 0 && event.getPrice() < price.toFloat()) {
                        matches = false
                    }

                    if (matches) {
                        filteredEventList.add(event)
                    }

                    recyclerView = findViewById(R.id.recyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this@ManualSearchActivity)


                    val eventAdapter = EventAdapter(filteredEventList, this@ManualSearchActivity) { event ->
                        onEventClick(event)
                    }

                    recyclerView.adapter = eventAdapter
                }
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }
    }

    /**
     * Carga la lista de eventos en el RecyclerView.
     */
    private fun loadEvents() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        lifecycleScope.launch {
            try {
                val events = getEvents()
                eventList = events?.toMutableList() as ArrayList<Event>

                val eventAdapter = EventAdapter(eventList, this@ManualSearchActivity) { event ->
                    onEventClick(event)
                }

                recyclerView.adapter = eventAdapter
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }
    }

    /**
     * Al hacer clic en un evento, se muestran sus detalles.
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

    /**
     * Activa la barra de navegacion
     */
    private fun activateNavBar() {
        val navChat = findViewById<ImageView>(R.id.navChatIcon)
        val navMain = findViewById<ImageView>(R.id.navReservaIcon)
        val navInventori = findViewById<ImageView>(R.id.navInventariIcon)
        val navSettings = findViewById<ImageView>(R.id.navProfileIcon)


        if (user.getType() == "Event Organizer") {
            val navSearch = findViewById<ImageView>(R.id.navSearchIcon)
            navSearch.setBackgroundResource(R.drawable.event_ic)
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

    private fun showDatePickerDialog(textView: TextView, dateFormatter: SimpleDateFormat) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                textView.text = dateFormatter.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
    }

}
