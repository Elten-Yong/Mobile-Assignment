package com.example.androidassignment

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.androidassignment.databinding.ActivityEditBankCardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.dialog_progress.*

class EditBankCardActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditBankCardBinding // viewbinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth // firebase auth
    val userId = FirebaseAuth.getInstance().currentUser.uid// pk
    //    var editClicked : Boolean = false
    lateinit var progressDialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_bank_card)

        binding = ActivityEditBankCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth //initialise firebase object

        //get data from fragment
        intent = getIntent()
        binding.txtProfileUsername.text = intent.getStringExtra("name")
        binding.editTextBankCard.setText(intent.getStringExtra("bank"), TextView.BufferType.EDITABLE)
//        getActivity()?.let {
//            Glide.with(it)
//                    .load(viewModel.profilePicture)
//                    .into(binding.ProfileImage)
//        }
        Glide.with(this).load(intent.getStringExtra("photo")).into(binding.profilePicture)

        binding.btnConfirmEdit.setOnClickListener{

            editBankCard()
        }

        binding.btnCancel.setOnClickListener{
            finish()
        }
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

    override fun onResume() {
        super.onResume()
        refreshBankCard()

    }

    fun editBankCard(){

        val bankCardNumber = binding.editTextBankCard

        if(bankCardNumber.text.toString().isEmpty()){
            bankCardNumber.error = "Please enter Card Number"
            bankCardNumber.requestFocus()
            return //error occur stop
        }

        if(bankCardNumber.text.toString().length < 16){
            bankCardNumber.error = "Please enter valid Card Number"
            bankCardNumber.requestFocus()
            return //error occur stop
        }


        showProgressDialog("Updating...")
        database = Firebase.database.reference

        database.child("users").child(userId).child("cardNumber").setValue((bankCardNumber.text).toString())

        Toast.makeText(this.applicationContext, "Succesfully updated", Toast.LENGTH_LONG).show()

        hideProgressDialog()

        finish()
    }

    fun refreshBankCard(){

        //getUserName
        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {


                binding.editTextBankCard.setText(snapshot.child("cardNumber").getValue().toString(),TextView.BufferType.EDITABLE)

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}