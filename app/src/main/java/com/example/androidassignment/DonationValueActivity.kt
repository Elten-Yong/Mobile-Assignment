package com.example.androidassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidassignment.databinding.DonationValueActivityBinding
import kotlinx.android.synthetic.main.donation_value_activity.*


class DonationValueActivity : AppCompatActivity() {

    lateinit var binding: DonationValueActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donation_value_activity)

        binding = DonationValueActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.donateSubmitBtn.setOnClickListener{
            testEmpty()

        }
    }

    fun testEmpty(){
        if(decimalTxt.text.toString().isEmpty()){
            decimalTxt.error = "Please enter amount"
            decimalTxt.requestFocus()
            return
        }
        startActivity(Intent(this, DonationMethodActivity::class.java))
    }
}