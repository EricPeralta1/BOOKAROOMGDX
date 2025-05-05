package com.example.bookaroom.android.Activities

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class CreateEventActivity  : AppCompatActivity() {
    private lateinit var user : User
    private var imageUri: Uri? = null
    internal var x1: Float = 0.toFloat()
    internal var x2: Float = 0.toFloat()
    internal var y1: Float = 0.toFloat()
    internal var y2: Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        user = intent.getParcelableExtra<User>("user")!!
        val imageSelect = findViewById<ImageView>(R.id.imageSelect)

        imageSelect.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, 1)
        }
        activateNavBar()
        initializeCalendars()
        val saveChanges = findViewById<TextView>(R.id.saveChanges)
        saveChanges.setOnClickListener {
            saveEvent()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            val selectedImageUri = data?.data
            val imageView = findViewById<ImageView>(R.id.imageSelect)
            imageView.setImageURI(selectedImageUri)
            imageUri = selectedImageUri
        }
    }

    /**
     * Guarda el evento creado.
     */
    private fun saveEvent() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val name: String = findViewById<EditText>(R.id.nameEditText).text.toString()
                val aforament: Int = findViewById<EditText>(R.id.surnameEditText).text.toString().toInt()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val startDate: String = findViewById<EditText>(R.id.startDateET).text.toString()
                val filterStartDate = dateFormat.parse(startDate)!!
                val endDate: String = findViewById<EditText>(R.id.endDateET).text.toString()
                val filterEndDate = dateFormat.parse(endDate)!!
                val sala: Int = findViewById<EditText>(R.id.salaET).text.toString().toInt()
                val preu: Float = findViewById<EditText>(R.id.priceET).text.toString().toFloat()
                val description : String = findViewById<EditText>(R.id.descEditText).toString()


                if (imageUri != null){
                    val fileName = imageUri?.let { getFileName(it) }!!
                    val event = Event(0, sala, user.getIdUser(), aforament, filterStartDate, filterEndDate, preu, name, description, fileName, 1)
                    val createdEvent = ApiRepository.createEvent(event, imageUri)
                    if (createdEvent != null) {
                        Toast.makeText(applicationContext, "Evento creado exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Error al crear el evento", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val event = Event(0, sala, user.getIdUser(), aforament, filterStartDate, filterEndDate, preu, name, description, " ", 1)
                    val createdEvent = ApiRepository.createEvent(event, imageUri)
                    if (createdEvent != null) {
                        Toast.makeText(applicationContext, "Evento creado exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Error al crear el evento", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                println("Error de conexión: ${e.message}")
            }
        }

    }

    fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) {
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }

    /**
     * Inicializa el calendario para seleccionas las fechas del evento. Permite que el formato devuelto sea
     * dd/MM/yyyy.
     */
    private fun initializeCalendars() {
        val startDate = findViewById<TextView>(R.id.startDateET)
        val endDate = findViewById<TextView>(R.id.endDateET)

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


        startDate.setOnClickListener {
            showDatePickerDialog(startDate, dateFormatter)
        }

        endDate.setOnClickListener {
            showDatePickerDialog(endDate, dateFormatter)
        }
    }

    /**
     * Permite que, al hacer clic en la seleccion de fecha, se abra un calendario.
     * Dicho calendario permite elegir una fecha y devolverla a la app.
     */
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

    /**
     * Activa la barra de navegación inferior.
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

}
