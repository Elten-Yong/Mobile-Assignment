package com.example.androidassignment

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding // viewbinding
    private lateinit var auth: FirebaseAuth // firebase auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth //initialise firebase object

        //viewbinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // Gets resize one
//        val layout: ConstraintLayout = binding.root
//// Gets the layout params that will allow you to resize the layout
//        val params: ViewGroup.LayoutParams = layout.layoutParams
//        params.width = 1000
//        params.height = 1000
//        layout.layoutParams = params

        //register onclick
        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

        binding.btnSignIn.setOnClickListener{
               doLogin()

        }

        binding.txtForgetPassword.setOnClickListener{
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
            finish()
        }

    }


    private fun doLogin(){
        val email = binding.editTxtEmail
        val password = binding.editTxtPassword

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

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)

                } else {
                    Toast.makeText(
                        baseContext, "Sign In Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser : FirebaseUser?){

        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

//     fun checkUser() : Boolean{
//         var isAdmin : Boolean = false
//        database = Firebase.database.reference // reference to database
//         val userId = FirebaseAuth.getInstance().currentUser.uid// pk
//
//        database.child("users").child(userId).child("type").get().addOnSuccessListener {
//            if(((it.value).toString()).equals("admin")){
//                isAdmin = true
//            }
//            Log.e("firebase", "Error getting data", $it.value)
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }
//         return isAdmin
//    }

    private fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }
}