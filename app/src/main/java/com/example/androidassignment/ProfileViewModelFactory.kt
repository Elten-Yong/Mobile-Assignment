package com.example.androidassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(private var userName : String,private var email : String, private var phoneNumber : String
,private var photo : String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(userName, email , phoneNumber, photo) as T
        }
        throw IllegalArgumentException("ViewModel Class not found.")
        TODO("Not yet implemented")
    }

}