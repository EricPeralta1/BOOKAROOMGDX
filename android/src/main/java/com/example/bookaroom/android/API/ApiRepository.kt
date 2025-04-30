package com.example.bookaroom.android.API

import com.example.bookaroom.Objects.Event
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
}
