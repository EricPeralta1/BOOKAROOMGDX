package com.example.bookaroom.android.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

class ChatActivity : AppCompatActivity() {

    private lateinit var user: User
    private lateinit var socket: Socket
    private lateinit var ois: ObjectInputStream
    private lateinit var oos: ObjectOutputStream
    private val chatMessages = mutableListOf<Message>()
    private lateinit var messagesAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_chat)

        user = intent.getParcelableExtra<User>("user")!!


        activateNavBar()
        showChatWarning()
        loadChat()
        setupSocket()

        val send = findViewById<ImageView>(R.id.sendIc)
        send.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupSocket() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                socket = Socket("10.0.2.2", 6103)
                oos = ObjectOutputStream(socket.outputStream)
                oos.flush()
                ois = ObjectInputStream(socket.inputStream)

                while (true) {
                    val messageJson = ois.readObject() as String
                    val gson = Gson()
                    val updatedMessages = gson.fromJson(messageJson, Array<Message>::class.java).toList()

                    withContext(Dispatchers.Main) {
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMessages)
                        recyclerView.layoutManager = LinearLayoutManager(this@ChatActivity)
                        val users = loadUsersFromJSON(loadJsonFromRaw(this@ChatActivity, R.raw.users)!!)
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

                    oos.writeObject(messageJson)
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

        loadChat()

    }

    private fun updateChat() {
        socket = Socket("localhost", 6100)


    }

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

    private fun showChatWarning() {
        val warningFrag = ChatWarningFrag()

        supportFragmentManager.beginTransaction()
            .replace(R.id.chat_contract, warningFrag)
            .addToBackStack(null)
            .commit()

        val shadow = findViewById<View>(R.id.shadowBgChat)
        val fragment = findViewById<FragmentContainerView>(R.id.chat_contract)

        shadow.visibility = View.VISIBLE
        fragment.visibility = View.VISIBLE
    }

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
