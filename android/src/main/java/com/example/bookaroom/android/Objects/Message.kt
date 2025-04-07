package com.example.bookaroom.Objects

import java.util.Date

class Message(
    val id : Int,
    val id_enviar : Int,
    val mensaje : String,
    val data_enviar : Date,
    val estat : String) {

    fun getMessage(): String {
        return mensaje
    }
}