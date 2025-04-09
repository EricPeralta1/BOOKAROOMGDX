package com.example.bookaroom.Objects

import android.os.Parcel
import android.os.Parcelable
import com.example.bookaroom.Objects.Event
import java.io.Serializable
import java.util.Date

class Message(
    val id : Int,
    val id_enviar : Int,
    val mensaje : String,
    val data_enviar : Date,
    val estat : String) : Serializable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        Date(parcel.readLong()),
        parcel.readString() ?: "",
    )

    fun getMessage(): String {
        return mensaje
    }


    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }
}
