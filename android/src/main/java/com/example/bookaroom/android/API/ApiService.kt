package com.example.bookaroom.android.API

import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.User
import com.example.bookaroom.android.Objects.ImageUploadRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @GET("api/esdeveniments")
    suspend fun getEvents(): Response<List<Event>>

    @GET("api/usuaris")
    suspend fun getUsers(): Response<List<User>>

    @POST("api/usuaris")
    suspend fun createUser(@Body user: User): Response<User>

    @POST("api/esdeveniments")
    suspend fun createEvent(@Body event: Event): Response<Event>

    @POST("uploadImage")
    suspend fun uploadEventImage(@Body request: ImageUploadRequest): Response<Unit>}
