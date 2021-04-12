package com.example.androidassignment

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding // viewbinding
    private lateinit var auth: FirebaseAuth // Authentication
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth //initialise firebase auth object

        //viewbinding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //login text onclick
        binding.txtLogIn.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // signup btn onlick
        binding.btnSignUp.setOnClickListener {
            signUp()

        }

        //forget password onclick

        binding.txtForgetPassword.setOnClickListener{
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
            finish()
        }

    }

    private fun signUp(){

        val username = binding.editTxtUsername
        val email = binding.editTxtEmail
        val password = binding.editTxtPassword
        val confirmPassword = binding.editTxtConfirmPassword


        if(username.text.toString().isEmpty()){
            username.error = "Please enter Username"
            username.requestFocus()
            return //error occur stop
        }

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

        if(password.text.toString().isEmpty()){
            password.error = "Please enter password"
            password.requestFocus()
            return
        }
        if(!isValidPassword(password.text.toString())){
            password.error = "Over 6 characters with alphabet and number please."
            password.requestFocus()
            return
        }

        if(!confirmPassword.text.toString().equals(password.text.toString())){
            confirmPassword.error = "Confirm password not the same"
            confirmPassword.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    database = Firebase.database.reference // reference to database
                    val userId = FirebaseAuth.getInstance().currentUser.uid// pk

                    writeNewUser(userId, username.text.toString(), email.text.toString()) // save into database

                    startActivity(Intent(this, LoginActivity::class.java)) // navigate to login
                    finish()

                } else {
                    Toast.makeText(
                            baseContext, "Sign Up Failed",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun writeNewUser(userId: String, name: String, email: String) {

        val type = "normal"
        val user = User(name, email,type) //user class

        database.child("users").child(userId).setValue(user)

    }

//    //add phonenumber
//    fun trytry(userId: String){
//        val phoneNumber = "0123456"
//
//        database.child("users").child(userId).child("phoneNumber").setValue(phoneNumber)
//
//    }

}