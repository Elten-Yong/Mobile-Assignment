package com.example.androidassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class DonationTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_type)
    }

    fun cash(view: View) {
        startActivity(Intent(this,DonationValueActivity::class.java))
    }

    fun item(view: View) {
        //startActivity(Intent(this,activity_location_list::class.java))
    }
}