package com.example.bookaroom.Objects

class Room(
    val id_sala: Int,
    private val idInventory: Int,
    val dimensions:Int,
    val maxCapacity:Int,
    val fixedseats:Boolean,
    val estate:Boolean,
    ){}
