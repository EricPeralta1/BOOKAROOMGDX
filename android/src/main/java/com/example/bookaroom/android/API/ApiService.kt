package com.example.bookaroom.android.API

import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/esdeveniments")
    suspend fun getEvents(): Response<List<Event>>

}
