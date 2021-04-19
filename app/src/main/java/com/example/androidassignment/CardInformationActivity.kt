package com.example.androidassignment

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.androidassignment.Community.CommunityActivity
import com.example.androidassignment.databinding.ActivityCardInformationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_card_information.*


class CardInformationActivity : AppCompatActivity() {

    lateinit var binding: ActivityCardInformationBinding


    private lateinit var auth: FirebaseAuth // Authentication
    private lateinit var database: DatabaseReference
    val userId = FirebaseAuth.getInstance().currentUser.uid// pk



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_information)


        supportActionBar?.title = "Card Information"

        auth = Firebase.auth //initialise firebase auth object

        binding = ActivityCardInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //Submit button
        binding.submitBtn.setOnClickListener {

            checkEmpty()


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

    fun checkEmpty(){
        database = Firebase.database.reference // reference to database
        val userId = FirebaseAuth.getInstance().currentUser.uid// pk

        if(inputName.text.toString().isEmpty()){
            inputName.error = "Please enter name"
            inputName.requestFocus()
            return //error occur stop
        }
        if(inputCardNumber.text.toString().isEmpty()){
            inputCardNumber.error = "Please enter Card Number"
            inputCardNumber.requestFocus()
            return //error occur stop
        }

        if(inputCardNumber.text.toString().length < 16){
            inputCardNumber.error = "Please enter valid Card Number"
            inputCardNumber.requestFocus()
            return //error occur stop
        }

        if(inputCVV.text.toString().isEmpty()){
            inputCVV.error = "Please enter CVV "
            inputCVV.requestFocus()
            return //error occur stop
        }

        if(inputCardNumber.text.toString().length < 3){
            inputCardNumber.error = "Please enter valid CVV"
            inputCardNumber.requestFocus()
            return //error occur stop
        }
        Toast.makeText(baseContext, "Donate Successful", Toast.LENGTH_SHORT).show()
        database.child("users").child(userId).child("cardNumber").setValue(binding.inputCardNumber.text.toString())
        startActivity(Intent(this,MainActivity::class.java))
    }

}