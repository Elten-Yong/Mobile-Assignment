package com.example.androidassignment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class information (var postID: String,var subject: String,var contentPost: String, var photoUpload: String): Parcelable{
/*    var postID: String? = null
    var subject: String? = null
    var contentPost: String? = null
    var photoUpload: String? = null*/

    constructor():this("","","","")

/*    init {
        this.postID = postID
        this.subject = subject
        this.contentPost = contentPost
        this.photoUpload = photoUpload
    }*/

}