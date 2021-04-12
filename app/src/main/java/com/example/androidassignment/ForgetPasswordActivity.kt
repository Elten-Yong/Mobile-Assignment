package com.example.androidassignment

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class ForgetPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgetPasswordBinding // viewbinding
    private lateinit var auth: FirebaseAuth // Authentication
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        //viewbinding
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //login text onclick
        binding.txtLogIn.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

        binding.btnSubmit.setOnClickListener{
            doReset()

        }

    }

    private fun doReset(){
        val email = binding.editTxtEmail

        if(email.text.toString().isEmpty()){
            email.error = "Please enter Email"
            email.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        //do reset
        FirebaseAuth.getInstance().sendPasswordResetEmail(email.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this, "Email succesfully sent!", Toast.LENGTH_LONG
                ).show()

                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            else{
                Toast.makeText(
                    this, task.exception!!.message.toString(), Toast.LENGTH_LONG
                ).show()
            }
        }
    }


}