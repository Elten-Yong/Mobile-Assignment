package com.example.androidassignment

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    private lateinit var database: DatabaseReference//firebase realtime
    val userId = FirebaseAuth.getInstance().currentUser.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adminInfoFragment = admin_info_center()
        val profileFragment = profile()
        val infoFragment = infoCenter()

        database = Firebase.database.reference // reference to database

        database.child("users").child(userId).child("type").get().addOnSuccessListener {
            if(((it.value).toString()).equals("admin")){
                Log.i("firebase111", " admin ")
                makeCurrentFragment(adminInfoFragment)

                bottom_navigation.setOnNavigationItemSelectedListener {

                    when (it.itemId){
                        R.id.infoCenter -> makeCurrentFragment(adminInfoFragment)
                        R.id.profile -> makeCurrentFragment(profileFragment)
                    }
                    true
                }

            }else {
                Log.i("firebase111", " normal ")
                makeCurrentFragment(infoFragment)

                bottom_navigation.setOnNavigationItemSelectedListener {

                    when (it.itemId){
                        R.id.infoCenter -> makeCurrentFragment(infoFragment)
                        R.id.profile -> makeCurrentFragment(profileFragment)
                    }
                    true
                }
            }
        }.addOnFailureListener{

        }




    }


    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}