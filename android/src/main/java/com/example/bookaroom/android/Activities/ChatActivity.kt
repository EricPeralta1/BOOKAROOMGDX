package com.example.bookaroom.android.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookaroom.Adapters.ChatAdapter
import com.example.bookaroom.Fragments.ChatWarningFrag
import com.example.bookaroom.Objects.Message
import com.example.bookaroom.Objects.User
import com.example.bookaroom.Objects.loadChatFromJSON
import com.example.bookaroom.Objects.loadJsonFromRaw
import com.example.bookaroom.Objects.loadUsersFromJSON
import com.example.bookaroom.R
import com.example.bookaroom.android.API.ApiRepository.getUsers
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStreamWriter
import java.net.Socket
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.abs

class ChatActivity : AppCompatActivity() {

    private lateinit var user: User
    private lateinit var socket: Socket
    private lateinit var ois: ObjectInputStream
    private lateinit var oos: ObjectOutputStream
    private val chatMessages = mutableListOf<Message>()
    private lateinit var messagesAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    internal var x1: Float = 0.toFloat()
    internal var x2: Float = 0.toFloat()
    internal var y1: Float = 0.toFloat()
    internal var y2: Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_chat)

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        user = intent.getParcelableExtra<User>("user")!!


        activateNavBar()
        showChatWarning()
        setupSocket()

        val send = findViewById<ImageView>(R.id.sendIc)
        send.setOnClickListener {
            sendMessage()
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

                if (abs(swipeX) > MIN_DISTANCE && abs(swipeY) < MIN_DISTANCE) {
                    if (swipeX > 0) {
                        val i = Intent(this, SearchEventActivity::class.java)
                        i.putExtra("user", user)
                        startActivity(i)
                        finish()
                    }
                }

                if (abs(swipeX) > MIN_DISTANCE && abs(swipeY) < MIN_DISTANCE) {
                    if (user.getType() == "Event Organizer"){
                        val i = Intent(this, CreateEventActivity::class.java)
                        i.putExtra("user", user)
                        startActivity(i)
                        finish()
                    } else {
                        val i = Intent(this, ManualSearchActivity::class.java)
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
     * Configura el socket para recibir mensajes del servidor de forma períodica (para actualizar los mensajes recientes)
     */
    private fun setupSocket() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                socket = Socket("10.0.2.2", 6103)
                oos = ObjectOutputStream(socket.outputStream)
                oos.flush()
                ois = ObjectInputStream(socket.inputStream)

                while (true) {
                    val messageJson = ois.readObject() as String
                    val decryptedMessage = decryptData(messageJson, "unlockPassword")
                    val gson = Gson()
                    val updatedMessages = gson.fromJson(decryptedMessage, Array<Message>::class.java).toList()

                    withContext(Dispatchers.Main) {
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMessages)
                        recyclerView.layoutManager = LinearLayoutManager(this@ChatActivity)
                        val users = getUsers()!!
                        val messagesAdapter = ChatAdapter(updatedMessages, users)
                        recyclerView.adapter = messagesAdapter
                        recyclerView.scrollToPosition(updatedMessages.size - 1)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Envía el mensaje que nosotros escribamos al servidor.
     */
    private fun sendMessage() {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val messageText = findViewById<EditText>(R.id.sendMessageEditText).text.toString()
                    val users = loadUsersFromJSON(loadJsonFromRaw(this@ChatActivity, R.raw.users)!!)
                    val currentDateTime = LocalDateTime.now()
                    val zonedDateTime = currentDateTime.atZone(ZoneOffset.UTC)
                    val date = Date.from(zonedDateTime.toInstant())

                    val messageObject = Message(1, users[0].getIdUser(), messageText, date, "sent")
                    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()

                    val messageJson = gson.toJson(messageObject)

                    val encryptedJson = encryptData(messageJson, "unlockPassword")

                    oos.writeObject(encryptedJson)
                    oos.flush()

                    withContext(Dispatchers.Main) {
                        val messageTextEdit = findViewById<EditText>(R.id.sendMessageEditText)
                        messageTextEdit.text.clear()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    private fun saveChatToJSON(chatMessages: List<Message>) {
        val gson = Gson()
        val chatJson = gson.toJson(chatMessages)
        val file = File(filesDir, "chat.json")

        try {
            val outputStream = FileOutputStream(file)
            val writer = OutputStreamWriter(outputStream)
            writer.write(chatJson)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun updateChat() {
        socket = Socket("localhost", 6100)


    }

    /**
     * Carga el chat.
     */
    private fun loadChat() {
        val file = File(this.filesDir, "chat.json")
        val chatMessages: List<Message>

        if (file.exists()) {
            val inputStream = FileInputStream(file)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = bufferedReader.use { it.readText() }

            val gson = Gson()
            val projectObjects = object : TypeToken<List<Message>>() {}.type
            chatMessages = gson.fromJson(jsonText, projectObjects)
        } else {
            chatMessages = loadChatFromJSON(loadJsonFromRaw(this, R.raw.chat)!!)
        }

        val users = loadUsersFromJSON(loadJsonFromRaw(this, R.raw.users)!!)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMessages)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val messagesAdapter = ChatAdapter(chatMessages, users)
        recyclerView.adapter = messagesAdapter
        recyclerView.scrollToPosition(chatMessages.size - 1)
    }

    /**
     * Muestra el fragment de aviso del chat.
     */
    private fun showChatWarning() {
        val warningFrag = ChatWarningFrag()
        val bundle = Bundle()
        bundle.putParcelable("user", user)
        warningFrag.arguments = bundle


        supportFragmentManager.beginTransaction()
            .replace(R.id.chat_contract, warningFrag)
            .addToBackStack(null)
            .commit()

        val shadow = findViewById<View>(R.id.shadowBgChat)
        val fragment = findViewById<FragmentContainerView>(R.id.chat_contract)

        shadow.visibility = View.VISIBLE
        fragment.visibility = View.VISIBLE
    }

    /**
     * Activa la navegación entre activities en la barra inferior.
     */
    private fun activateNavBar() {
        val navSearch = findViewById<ImageView>(R.id.navSearchIcon)
        val navMain = findViewById<ImageView>(R.id.navReservaIcon)
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

    @OptIn(ExperimentalEncodingApi::class)
    private fun encryptData(plainText: String, key: String): String {
        try {
            val keyBytes = key.padEnd(32, ' ').substring(0, 32).toByteArray(Charsets.UTF_8)
            val iv = ByteArray(16)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

            val secretKeySpec = SecretKeySpec(keyBytes, "AES")
            val ivParameterSpec = IvParameterSpec(iv)

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

            return Base64.encode(encryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error al encriptar datos", e)
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun decryptData(encryptedText: String, key: String): String {
        try {
            val keyBytes = key.padEnd(32, ' ').substring(0, 32).toByteArray(Charsets.UTF_8)
            val iv = ByteArray(16)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

            val secretKeySpec = SecretKeySpec(keyBytes, "AES")
            val ivParameterSpec = IvParameterSpec(iv)

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

            val encryptedBytes = Base64.decode(encryptedText)
            val decryptedBytes = cipher.doFinal(encryptedBytes)

            return String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error al desencriptar datos", e)
        }
    }
}
