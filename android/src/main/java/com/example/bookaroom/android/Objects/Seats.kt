package com.example.bookaroom.Objects

class Seats(
    val seat_id: Int,
    val room_id:Int,
    val row_number:Int,
    private val seat_number: Int) {

    fun getSeatId(): Int {
        return seat_id
    }

}
