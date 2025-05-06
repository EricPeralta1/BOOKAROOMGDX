package com.example.bookaroom.android.Objects

import android.os.Parcel
import android.os.Parcelable
import com.example.bookaroom.Objects.Event
import java.util.Date

class ImageUploadRequest(
    val imageName: String,
    val base64Image: String) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        )
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<ImageUploadRequest> {
        override fun createFromParcel(parcel: Parcel): ImageUploadRequest {
            return ImageUploadRequest(parcel)
        }

        override fun newArray(size: Int): Array<ImageUploadRequest?> {
            return arrayOfNulls(size)
        }
    }
}
