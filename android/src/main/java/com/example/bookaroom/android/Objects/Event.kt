package com.example.bookaroom.Objects

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Event(
    val id_esdeveniment: Int,
    val id_sala: Int,
    val id_usuari: Int,
    val aforament: Int,
    val data_inici: Date,
    val data_fi: Date,
    val foto_event: String,
    val preu: Float,
    val nombre: String,
    val descripcio: String): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
        )

    fun getImageByte(): String {
        return foto_event
    }

    fun getTitle(): String {
        return nombre
    }

    fun getPrice(): Float {
        return preu
    }

    fun getDecription(): String {
        return descripcio
    }

    fun getDataInici(): Date {
        return data_inici
    }

    fun getDataFinal(): Date {
        return data_fi
    }

    fun getIdSala(): Int {
        return id_sala
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