package com.example.bookaroom.android.API

import com.example.bookaroom.Objects.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}
