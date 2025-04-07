package com.example.bookaroom.Objects

import android.os.Parcel
import android.os.Parcelable

class Ticket(
    private val id_entrada: Int,
    private val id_usuari: Int,
    private val id_esdeveniment: Int,
    private val id_butaca: Int,
    private var estat:Int
) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
  )

    fun getIdEvent() : Int{
        return id_esdeveniment
    }

    fun getEstat() : Int{
        return estat
    }

    fun getId() : Int{
        return id_entrada
    }

    fun setId(id: Int) {
        this.estat = id
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Ticket> {
        override fun createFromParcel(parcel: Parcel): Ticket {
            return Ticket(parcel)
        }

        override fun newArray(size: Int): Array<Ticket?> {
            return arrayOfNulls(size)
        }
    }
}