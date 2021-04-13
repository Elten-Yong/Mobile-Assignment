package com.example.androidassignment

import android.widget.EditText
import com.google.firebase.database.IgnoreExtraProperties

//@IgnoreExtraProperties
class UserPost( topic: String, description: String, photoUpload: String) {
    var topic: String? = null
    var description: String? = null
    var photoUpload: String? = null

    constructor():this("","","")

    init {
        this.topic = topic
        this.description = description
        this.photoUpload = photoUpload
    }
}