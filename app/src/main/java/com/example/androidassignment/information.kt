package com.example.androidassignment

class information (postID: String,subject: String,contentPost: String, photoUpload: String){
    var postID: String? = null
    var subject: String? = null
    var contentPost: String? = null
    var photoUpload: String? = null

    constructor():this("","","","")

    init {
        this.postID = postID
        this.subject = subject
        this.contentPost = contentPost
        this.photoUpload = photoUpload
    }

}