package com.example.bookaroom.android.Activities

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
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.ObjectOutputStream
import java.io.OutputStreamWriter
import java.net.Socket
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var user : User
    private lateinit var socket : Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_chat)

        user = intent.getParcelableExtra<User>("user")!!


        activateNavBar()
        showChatWarning()
        loadChat()
        val send = findViewById<ImageView>(R.id.sendIc)
        send.setOnClickListener {
            sendMessage()
            val messageText = findViewById<EditText>(R.id.sendMessageEditText).text.clear()
        }
    }

    private fun sendMessage() {
        val messageText = findViewById<EditText>(R.id.sendMessageEditText).text
        val users = loadUsersFromJSON(loadJsonFromRaw(this, R.raw.users)!!)
        val currentDateTime = LocalDateTime.now()

        val date = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant())

        try {
            socket = Socket("localhost", 6102)
            val messageObject = Message(users.size+1, users[0].getIdUser(), messageText.toString(), date, "sent")

            val out = ObjectOutputStream(socket.outputStream)
            out.writeObject(messageObject)
            out.flush()
        }catch (exception : IOException){
            exception.toString()
        }


        //val file = File(this.filesDir, "chat.json")
        //var chatMessages: MutableList<Message> = mutableListOf()
        //if (file.exists()) {
        //    val inputStream = FileInputStream(file)
        //    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        //    val jsonText = bufferedReader.use { it.readText() }
        //    val gson = Gson()
        //    val projectObjects = object : TypeToken<List<Message>>() {}.type
        //    chatMessages = gson.fromJson(jsonText, projectObjects)
        //} else {
        //    chatMessages = loadChatFromJSON(loadJsonFromRaw(this, R.raw.chat)!!)
        //}
        //chatMessages.add(messageObject)
        //saveChatToJSON(chatMessages)
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
        socket = Socket("localhost", 6101)

        val inputStream = socket.getInputStream()

        val reader = BufferedReader(InputStreamReader(inputStream))

        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line).append("\n")
        }

        socket.close()

        val jsonMessages = response.toString()
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
