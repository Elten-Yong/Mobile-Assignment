package com.example.androidassignment.Community

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserPost(var topic: String, var description: String, var photoUpload: String, var userID: String, var postID: String) : Parcelable {
    /*var topic: String? = null
    var description: String? = null
    var photoUpload: String? = null*/

    constructor():this("","","","","")

    /*init {
        this.topic = topic
        this.description = description
        this.photoUpload = photoUpload
        this.userID = userID
    }*/
}