package com.example.androidassignment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        lateinit var binding: ActivityAboutUsBinding // viewbinding

        //viewbinding
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar

        actionBar!!.title = "About Us"
        actionBar.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}