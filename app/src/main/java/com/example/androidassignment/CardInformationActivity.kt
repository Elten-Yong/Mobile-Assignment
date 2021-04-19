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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_card_information.*


class CardInformationActivity : AppCompatActivity() {

    lateinit var binding: ActivityCardInformationBinding


    private lateinit var auth: FirebaseAuth // Authentication
    private lateinit var database: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_information)


        supportActionBar?.title = "Card Information"

        auth = Firebase.auth //initialise firebase auth object

        binding = ActivityCardInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Submit button
        binding.submitBtn.setOnClickListener {
            database = Firebase.database.reference // reference to database
            val userId = FirebaseAuth.getInstance().currentUser.uid// pk

            database.child("users").child(userId).child("cardNumber").setValue(binding.inputCardNumber.text.toString())
        }

        //binding.inputCardNumber.set

        //pull down list
        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?,view: View?, position: Int, id: Long) {
                monthTxt.text = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                monthTxt.text = "none"
            }
        }

        ArrayAdapter.createFromResource(this,R.array.month, android.R.layout.simple_spinner_item).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMonth.adapter = arrayAdapter
        }

        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?,view: View?, position: Int, id: Long) {
                yearTxt.text = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                yearTxt.text = "none"
            }
        }

        ArrayAdapter.createFromResource(this,R.array.year, android.R.layout.simple_spinner_item).also { arrayAdapter1 ->
            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerYear.adapter = arrayAdapter1
        }


    }



}