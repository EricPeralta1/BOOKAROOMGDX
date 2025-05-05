package com.example.bookaroom.Objects

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import java.util.Date

data class Event(
    val event_id: Int,
    val room_id: Int,
    val user_id: Int,
    val capacity: Int,
    val start_date: Date,
    val end_date: Date,
    private val price: Float,
    val name: String,
    val description: String,
    val event_image: String,
    val active : Int
   ): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.readFloat(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        )

    fun getImageByte(): String {
        return event_image
    }

    fun getTitle(): String {
        return name
    }

    fun getPrice(): Float {
        return price
    }

    fun getDecription(): String {
        return description
    }

    fun getDataInici(): Date {
        return start_date
    }

    fun getDataFinal(): Date {
        return end_date
    }

    fun getIdSala(): Int {
        return room_id
    }


    fun getIdUser(): Int {
        return user_id
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }



    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}
