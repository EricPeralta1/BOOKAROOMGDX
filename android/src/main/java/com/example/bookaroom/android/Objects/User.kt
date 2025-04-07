package com.example.bookaroom.Objects

import android.os.Parcel
import android.os.Parcelable

class User(
    val id_usuari : Int,
    private var nom : String,
    private var cognom : String,
    private var email : String,
    private var pass : String,
    private val rol : String) : Parcelable {

        constructor(parcel : Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!)

    fun getIdUser(): Int {
        return id_usuari
    }

    fun getEmail(): String {
        return email
    }

    fun getPass(): String {
        return pass
    }

    fun getName(): String {
        return nom
    }

    fun getSurname(): String {
        return cognom
    }

    fun getType(): String {
        return rol
    }

    fun setNom(nom: String) {
        this.nom = nom
    }

    fun setCognom(cognom: String) {
        this.cognom = cognom
    }
    fun setEmail(email: String) {
        this.email = email
    }
    fun setPass(pass: String) {
        this.pass = pass
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id_usuari)
        dest.writeString(nom)
        dest.writeString(cognom)
        dest.writeString(email)
        dest.writeString(pass)
        dest.writeString(rol)
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