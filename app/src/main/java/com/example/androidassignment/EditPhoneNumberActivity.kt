package com.example.androidassignment

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.androidassignment.databinding.ActivityEditPhoneNumberBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.dialog_progress.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class EditPhoneNumberActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditPhoneNumberBinding // viewbinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth // firebase auth
    val userId = FirebaseAuth.getInstance().currentUser.uid// pk
//    var editClicked : Boolean = false
    lateinit var progressDialog :Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_phone_number)
        //viewbinding
        binding = ActivityEditPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth //initialise firebase object

        //get data from fragment
        intent = getIntent()
        binding.txtProfileUsername.text = intent.getStringExtra("name")
        binding.editTextPhoneNumber.setText(intent.getStringExtra("phone"), TextView.BufferType.EDITABLE)
//        getActivity()?.let {
//            Glide.with(it)
//                    .load(viewModel.profilePicture)
//                    .into(binding.ProfileImage)
//        }
        Glide.with(this).load(intent.getStringExtra("photo")).into(binding.profilePicture)

        binding.btnConfirmEdit.setOnClickListener{

            editPhoneNumber()
        }

        binding.btnCancel.setOnClickListener{
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
            refreshPhoneNumber()

    }

    //progress bar
    fun showProgressDialog(text:String){
        progressDialog = Dialog(this)

        progressDialog.setContentView(R.layout.dialog_progress) // create one later

        progressDialog.txtDialogProgress.text = text

        progressDialog.setCancelable(false)

        progressDialog.setCanceledOnTouchOutside(false)

        progressDialog.show()
    }

    fun hideProgressDialog(){
        progressDialog.dismiss()
    }

    fun editPhoneNumber(){

        val phoneNumber = binding.editTextPhoneNumber

        if(!isValidPhoneNumber(phoneNumber.text.toString())){
            phoneNumber.error = "Proper phone format required"
            phoneNumber.requestFocus()
            return
        }

        showProgressDialog("Updating...")
        database = Firebase.database.reference

        database.child("users").child(userId).child("phone").setValue((phoneNumber.text).toString())

        Toast.makeText(this.applicationContext, "Succesfully updated", Toast.LENGTH_LONG).show()

        hideProgressDialog()

        finish()
    }

    fun refreshPhoneNumber(){

        //getUserName
        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {


                binding.editTextPhoneNumber.setText(snapshot.child("phone").getValue().toString(),TextView.BufferType.EDITABLE)

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun isValidPhoneNumber(phoneNumber: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(\\+?6?01)[0-46-9]-*[0-9]{7,8}\$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(phoneNumber)
        return matcher.matches()
    }

}