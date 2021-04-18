package com.example.androidassignment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class donationMethodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_method)
    }

    fun cardInformation(view: View) {
        startActivity(Intent(this,activity_card_information::class.java))
    }


}