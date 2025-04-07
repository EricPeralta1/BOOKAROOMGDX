package com.example.bookaroom.Objects

import android.content.Context
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

fun loadJsonFromRaw(context: Context, rawResId: Int): String? {
    return try {
        val inputStream = context.resources.openRawResource(rawResId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()

        reader.use { it.forEachLine { line -> stringBuilder.append(line).append("\n") } }

        stringBuilder.toString().trim()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun loadEventsFromJSON(json: String): ArrayList<Event> {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    val gson = GsonBuilder()
        .setDateFormat(dateFormat.toPattern())
        .create()

    val eventsListType = object : TypeToken<ArrayList<Event>>() {}.type

    return gson.fromJson(json, eventsListType)
}

fun loadTicketsFromJSON(json: String): ArrayList<Ticket> {

    val gson = Gson()

    val ticketsListType = object : TypeToken<ArrayList<Ticket>>() {}.type

    return gson.fromJson(json, ticketsListType)
}

fun loadChatFromJSON(json: String): ArrayList<Message> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    val gson = GsonBuilder()
        .setDateFormat(dateFormat.toPattern())
        .create()

    val messageListType = object : TypeToken<ArrayList<Message>>() {}.type

    return gson.fromJson(json, messageListType)
}

fun loadUsersFromJSON(json: String): ArrayList<User> {

    val gson = Gson()

    val usersListType = object : TypeToken<ArrayList<User>>() {}.type

    return gson.fromJson(json, usersListType)
}
