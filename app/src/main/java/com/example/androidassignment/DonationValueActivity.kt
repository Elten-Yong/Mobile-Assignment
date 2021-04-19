package com.example.androidassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidassignment.databinding.DonationValueActivityBinding


class DonationValueActivity : AppCompatActivity() {

    lateinit var binding: DonationValueActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donation_value_activity)

        binding = DonationValueActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.donateSubmitBtn.setOnClickListener{
            startActivity(Intent(this, DonationMethodActivity::class.java))
        }
    }

}