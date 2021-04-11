package com.example.androidassignment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding // viewbinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //viewbinding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //login text onclick
        binding.txtLogIn.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }


    }


}