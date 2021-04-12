package com.example.androidassignment

import android.widget.EditText
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserPost(val topic: EditText? = null, val description: EditText? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}