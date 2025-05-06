package com.example.bookaroom.android.API

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import com.example.bookaroom.android.Objects.ImageUploadRequest
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

    suspend fun createEvent(event: Event): Event? = withContext(Dispatchers.IO) {
        val response = apiService.createEvent(event)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun uploadEventImage(imageName: String, base64Image: String): Boolean = withContext(Dispatchers.IO) {
        val request = ImageUploadRequest(imageName, base64Image)
        val response = apiService.uploadEventImage(request)
        return@withContext response.isSuccessful
    }
}
