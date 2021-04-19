package com.example.androidassignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel(
    userName: String, email :String , phoneNumber : String,
    photo : String, card : String

) : ViewModel() {


    // TODO: Implement the ViewModel

//   private val _userNameL = MutableLiveData<String>()
//    val userNameL : LiveData<String>
//        get() = _userNameL
    var profilePicture : String = photo
    var profileEmail : String = email
    var profileUserName : String = userName
    var cardNumber : String = card

    //var profilePhoneNumber : String = phoneNumber

    //livedata
    private val _profilePhoneNumber = MutableLiveData<String>()
    val profilePhoneNumber : LiveData<String>
    get() = _profilePhoneNumber

    init{
        profileUserName = userName
        profileEmail = email
        //profilePhoneNumber = phoneNumber
        //live
        cardNumber = card
        _profilePhoneNumber.value = phoneNumber
        profilePicture  = photo

        Log.d("firebase555", "end: " + (profilePicture));
    }




//
//    @JvmName("getUserName1")
//    fun getUserName() {
//
//                val userId = FirebaseAuth.getInstance().currentUser.uid// pk
//                Log.d("firebase555", "Value: " + (userId));
//                //getUserName
//                val ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("username")
//                ref.addListenerForSingleValueEvent(object : ValueEventListener {
//
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        Log.d("firebase555", "Value: " + (snapshot.getValue().toString()))
//
//                        profileUserName = (snapshot.getValue().toString())
//
//                        Log.d("firebase555", "_userName: " + (snapshot.getValue().toString()))
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//                })
//                Log.d("firebase555", "end: " + (userNameL));
//            }
    }