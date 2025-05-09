package com.example.bookaroom.android.API

import com.example.bookaroom.Objects.Event
import com.example.bookaroom.Objects.Room
import com.example.bookaroom.Objects.Seats
import com.example.bookaroom.Objects.Ticket
import com.example.bookaroom.Objects.User
import com.example.bookaroom.Seat
import com.example.bookaroom.android.Objects.ImageUploadRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.sql.Date

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
    suspend fun uploadEventImage(@Body request: ImageUploadRequest): Response<Unit>

    @PUT("api/Usuaris/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): Response<User>

    @GET("api/entrades/fromevent/{eventId}")
    suspend fun getTicketsFromEvent(@Path("eventId") eventId: Int): Response<List<Ticket>>

    @POST("api/Entrades")
    suspend fun createTicket(@Body ticket: Ticket): Response<Ticket>

    @GET("api/entrades/fromuser/{userId}")
    suspend fun getTicketsFromUser(@Path("userId") userId: Int): Response<List<Ticket>>

    @PUT("api/entrades/{id}")
    suspend fun cancelEntrada(@Path("id") id: Int, @Body ticket: Ticket): Response<Ticket>

    @GET("api/Sales/availableRooms/{startDate}/{endDate}")
    suspend fun getAvailableRooms(@Path("startDate") startDate: Date, @Path("endDate") endDate: Date): Response<List<Room>>

    @GET("api/Butacas/fromroom/{roomId}")
    suspend fun getSeatsFromRoom(@Path("roomId") roomId: Int): Response<List<Seats>>
}


