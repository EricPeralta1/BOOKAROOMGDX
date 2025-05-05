package com.example.bookaroom.android.API

import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @GET("esdeveniments")
    suspend fun getEvents(): Response<List<Event>>

    @GET("usuaris")
    suspend fun getUsers(): Response<List<User>>

    @POST("usuaris")
    suspend fun createUser(@Body user: User): Response<User>

    @Multipart
    @POST("events")
    suspend fun createEvent(
        @Part("eventDetails") eventDetails: RequestBody,
        @Part image: MultipartBody.Part?): Response<Event>
}
