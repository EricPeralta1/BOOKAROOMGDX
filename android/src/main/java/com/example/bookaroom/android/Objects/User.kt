package com.example.bookaroom.Objects

import android.os.Parcel
import android.os.Parcelable

class User(
    val user_id : Int,
    private var name : String,
    private var surname : String,
    private var email : String,
    private var password : String,
    private val role : String,
    private var active : Int
    ) : Parcelable {

        constructor(parcel : Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt()
        )

    fun getIdUser(): Int {
        return user_id
    }

    fun getEmail(): String {
        return email
    }

    fun getPass(): String {
        return password
    }

    fun getName(): String {
        return name
    }

    fun getSurname(): String {
        return surname
    }

    fun getType(): String {
        return role
    }

    fun setNom(nom: String) {
        this.name = nom
    }

    fun setCognom(cognom: String) {
        this.surname = cognom
    }
    fun setEmail(email: String) {
        this.email = email
    }
    fun setPass(pass: String) {
        this.password = pass
    }
    fun setActive(active: Int) {
        this.active = active
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(user_id)
        dest.writeString(name)
        dest.writeString(surname)
        dest.writeString(email)
        dest.writeString(password)
        dest.writeString(role)
        dest.writeInt(active)
    }


    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
