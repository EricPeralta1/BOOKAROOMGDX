package com.example.bookaroom.android.Activities

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository
import com.example.bookaroom.android.Activities.LoginActivity
import com.google.android.gms.common.api.Api
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
        val imageSelect = findViewById<TextView>(R.id.gallerybutton)
        imageSelect.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, 1)
        }
        activateNavBar()
        initializeCalendars()
        initializeSeekBar()
        val saveChanges = findViewById<TextView>(R.id.saveChanges)
        saveChanges.setOnClickListener {
            saveEvent()
        }

        val getRooms = findViewById<TextView>(R.id.availableRooms)
        getRooms.setOnClickListener {
            loadAvailableRooms()
        }
    }

    /**
     * Permite navegar entre actividades al deslizar entre izquierda y derecha.
     */
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
     * Al coger una foto de la galeria, la devuelve como resultado y guarda la imagen en
     * el imageView correspondiente. Además, guarda la ruta de la imagen.
     */
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
                var creationSuccess = false
                val name: Editable = findViewById<EditText>(R.id.nameEditText).text
                val aforament: CharSequence = findViewById<TextView>(R.id.capacityNumber).text
                val sala: Any? = findViewById<Spinner>(R.id.salaET).selectedItem
                val preu: Editable = findViewById<EditText>(R.id.priceET).text
                val description : Editable = findViewById<EditText>(R.id.descEditText).text
                val startDate: CharSequence = findViewById<TextView>(R.id.startDateET).text
                val endDate: CharSequence = findViewById<TextView>(R.id.endDateET).text

                if (name.isEmpty() || aforament.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || sala?.equals(null) == true || preu.isEmpty() || description.isEmpty()){
                    Toast.makeText(this@CreateEventActivity, "Fill all fields before creating an event.", Toast.LENGTH_SHORT).show()
                } else {
                    if (preu.toString().toIntOrNull() == null || aforament.toString().toIntOrNull() == null){
                        Toast.makeText(this@CreateEventActivity, "Both price and capacity must be numbers. Don't enter any letters or special characters.", Toast.LENGTH_SHORT).show()
                    } else if (aforament.toString().toInt() > 30 || aforament.toString().toInt() <= 0){
                        Toast.makeText(this@CreateEventActivity, "Capacity must be from 1 to 30", Toast.LENGTH_SHORT).show()
                    } else {
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val filterStartDate = dateFormat.parse(startDate.toString())
                        val filterEndDate = dateFormat.parse(endDate.toString())!!
                        if (filterEndDate.before(filterStartDate)){
                            Toast.makeText(this@CreateEventActivity, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                        } else if (imageUri != null){
                            val fileName = imageUri?.let { getFileName(it) }!!
                            val filterName = fileName.replace("-" , "")
                            val imageString = imageTransform()!!
                            val event = Event(0, sala.toString().toInt(), user.getIdUser(), aforament.toString().toInt(), filterStartDate, filterEndDate, preu.toString().toFloat(), name.toString(), description.toString(), filterName, 1)
                            ApiRepository.uploadEventImage(filterName, imageString)
                            ApiRepository.createEvent(event)
                            Toast.makeText(this@CreateEventActivity, "Event created! Have fun!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@CreateEventActivity, SearchEventActivity::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            finish()

                        } else {
                            val event = Event(0, sala.toString().toInt(), user.getIdUser(), aforament.toString().toInt(), filterStartDate, filterEndDate, preu.toString().toFloat(), name.toString(), description.toString(), "noimage", 1)
                            ApiRepository.createEvent(event)
                            creationSuccess = true


                        }
                    }
                }

                if (creationSuccess){
                    Toast.makeText(this@CreateEventActivity, "Event created! Have fun!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@CreateEventActivity, SearchEventActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }

    }

    /**
     * Inicia SeekBar, una barra deslizante que permite seleccionar la capacidad del evento según el valor
     * que tenga la barra.
     */
    private fun initializeSeekBar() {
        val seekBar = findViewById<SeekBar>(R.id.capacitySeekBar)
        seekBar.max = 30
        seekBar.progress = 0

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val price = findViewById<TextView>(R.id.capacityNumber)
                price.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


    }

    /**
     * Transforma la imagen a un string de Base64 para enviarla a la API y reconstruirla
     * y guardarla.
     */
    private fun imageTransform() : String?{
        return try {
            val inputStream = contentResolver.openInputStream(imageUri!!)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            if (bytes != null) {
                android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Permite coger el nombre de la imagen. Elimina caracteres especiales.
     */
    private fun getFileName(uri: Uri): String {
        val context = this
        var result = "image.jpg"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = it.getString(index)
                }
            }
        }
        return result
    }

    /**
     * Carga las salas disponibles para hacer el evento entre los dias establecidos.
     */
    private fun loadAvailableRooms(){
        lifecycleScope.launch {
            val startDate = findViewById<TextView>(R.id.startDateET).text.toString()
            val endDate = findViewById<TextView>(R.id.endDateET).text.toString()

            if (startDate.isEmpty()|| endDate.isEmpty()){
                Toast.makeText(this@CreateEventActivity, "Make sure to fill dates before checking rooms!", Toast.LENGTH_SHORT).show()
            } else {
                val inputDateFormat = SimpleDateFormat("dd/MM/yyyy")
                val startDateParsed = inputDateFormat.parse(startDate)
                val endDateParsed = inputDateFormat.parse(endDate)
                if (endDateParsed.before(startDateParsed)){
                    Toast.makeText(this@CreateEventActivity, "End date can't be before start!", Toast.LENGTH_SHORT).show()
                } else {
                    val rooms = mutableListOf<Int>()
                    val availableRooms = ApiRepository.getAvailableRooms(startDate, endDate)!!
                    for (room in availableRooms){
                        rooms.add(room.getRoomId())
                    }
                    val spinner = findViewById<Spinner>(R.id.salaET)
                    val adapter = ArrayAdapter(this@CreateEventActivity, android.R.layout.simple_spinner_item, rooms)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
            }
        }
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
