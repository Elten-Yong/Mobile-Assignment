package com.example.androidassignment

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.androidassignment.Community.CommunityActivity
import com.example.androidassignment.databinding.ActivityCardInformationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_card_information.*


class CardInformationActivity : AppCompatActivity() {

    lateinit var binding: ActivityCardInformationBinding


    private lateinit var auth: FirebaseAuth // Authentication
    lateinit var filepath: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_information)


        supportActionBar?.title = "Card Information"

        auth = Firebase.auth //initialise firebase auth object

        binding = ActivityCardInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Submit button
        binding.submitBtn.setOnClickListener {

        }



    }

}