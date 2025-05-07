package com.example.bookaroom.Objects

import android.os.Parcel
import android.os.Parcelable

class Ticket(
    private val ticket_id: Int,
    private val user_id: Int,
    private val event_id: Int,
    private val seat_id: Int,
    private var status:Int
) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
  )

    fun getIdEvent() : Int{
        return event_id
    }

    fun getEstat() : Int{
        return status
    }

    fun getId() : Int{
        return ticket_id
    }

    fun setId(id: Int) {
        this.status = id
    }

    fun setStatus(status: Int) {
        this.status = status
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ticket_id)
        dest.writeInt(user_id)
        dest.writeInt(event_id)
        dest.writeInt(seat_id)
        dest.writeInt(status)
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
