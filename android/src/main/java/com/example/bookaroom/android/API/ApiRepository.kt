package com.example.bookaroom.android.API

import android.net.Uri
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object ApiRepository {
    private val apiService = ApiClient.apiService

    suspend fun getEvents(): List<Event>? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getEvents()
            if (response.isSuccessful) response.body() else null
        }
    }

    suspend fun getUsers(): List<User>? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getUsers()
            if (response.isSuccessful) response.body() else null
        }
    }

    suspend fun createUser(user: User): User? = withContext(Dispatchers.IO) {
        val response = apiService.createUser(user)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun createEvent(event: Event, imageUri: Uri?): Event? {
        return withContext(Dispatchers.IO) {
            val eventDetails = RequestBody.create(MediaType.parse("application/json"), event.toJson())
            val imagePart = imageUri?.let {
                val file = File(it.path!!)
                val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                MultipartBody.Part.createFormData("image", file.name, requestBody)
            }

            val response = apiService.createEvent(eventDetails, imagePart)
            if (response.isSuccessful) response.body() else null
        }
    }
}
