package com.example.bookaroom.android.API

import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.Ticket
import com.example.bookaroom.Objects.User
import com.example.bookaroom.android.Objects.ImageUploadRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun updateUser(id: Int, user: User): Boolean = withContext(Dispatchers.IO) {
        val response = apiService.updateUser(id, user)
        return@withContext response.isSuccessful && response.code() == 204
    }

    suspend fun getTicketsFromEvent(eventId: Int): List<Ticket>? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getTicketsFromEvent(eventId)
            if (response.isSuccessful) response.body() else null
        }
    }

    suspend fun getTicketsFromUser(userId: Int): List<Ticket>? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getTicketsFromUser(userId)
            if (response.isSuccessful) response.body() else null
        }
    }

    suspend fun createReserva(ticket: Ticket): Ticket? = withContext(Dispatchers.IO) {
        val response = apiService.createTicket(ticket)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun cancelEntrada(id: Int, ticket: Ticket): Boolean = withContext(Dispatchers.IO) {
        val response = apiService.cancelEntrada(id, ticket)
        return@withContext response.isSuccessful && response.code() == 204
    }
}
