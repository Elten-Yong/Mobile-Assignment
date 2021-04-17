package com.example.androidassignment.Community

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Comment(var username: String, var text: String, var profilePic: String, var time: String) : Parcelable {

    constructor():this("","","", "")

    /*init {
        this.username = username
        this.text = text
        this.profilePic = profilePic
        this.time = time
    }*/
}