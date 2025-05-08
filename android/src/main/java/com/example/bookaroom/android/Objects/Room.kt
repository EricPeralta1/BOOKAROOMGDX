package com.example.bookaroom.Objects

class Room(
    val room_id: Int,
    private val inventory_id: Int,
    val dimensions:Int,
    val max_capacity:Int,
    val num_seats: Int,
    val status:Int,
    ){

    fun getRoomId(): Int {
        return room_id
    }
}
