package com.example.androidassignment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.androidassignment.databinding.ProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.profile_fragment.*
import java.util.*


class profile : Fragment() {
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private var profileUserName :String = ""
    private var profileEmail :String = ""
    private var profilePhoneNumber :String = ""
    private var isFirstTime : Boolean = true
    private var profilePicture : String = ""
    private var bankCard : String = ""
    val userId = FirebaseAuth.getInstance().currentUser.uid// pk
//    var resolver = activity!!.contentResolver
    var selectedImages: String? = null
    var filepath: Uri? = null
    lateinit var progressDialog :Dialog
    private var isEditProfilePicture : Boolean = false


    companion object {
        fun newInstance() = profile()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getUserInfo()

    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnSignOut.setOnClickListener{
            Toast.makeText(activity?.applicationContext, "Successfully SignOut", Toast.LENGTH_LONG).show()

            Firebase.auth.signOut()

            startActivity(Intent(getActivity(), LoginActivity::class.java))

            getActivity()?.finish()
        }

        binding.phoneEdit.setOnClickListener{

            val intent = Intent(getActivity(), EditPhoneNumberActivity::class.java)
            intent.putExtra("name", profileUserName)
            intent.putExtra("phone", profilePhoneNumber)
            intent.putExtra("photo", profilePicture)
            startActivity(intent)
        }

        binding.bankCardEdit.setOnClickListener{
            val intent = Intent(getActivity(), EditBankCardActivity::class.java)
            intent.putExtra("name", profileUserName)
            intent.putExtra("bank", bankCard)
            intent.putExtra("photo", profilePicture)
            startActivity(intent)
        }

        binding.iconInfo.setOnClickListener{
            startActivity(Intent(getActivity(), AboutUsActivity::class.java))
        }

        binding.ProfileImage.setOnClickListener{

            //intent to select picture
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            startActivityForResult(intent, 111)
        }

        binding.iconHistory.setOnClickListener{
            startActivity(Intent(getActivity(), ViewHistoryActivity::class.java))

        }

        binding.iconBookmark.setOnClickListener {
            startActivity(Intent(getActivity(), BookmarkActivity::class.java))

        }

        return view
    }



//    private fun setUsername(){
//        //observe what state
//        //A lambda expression is an anonymous function that isn't declared,
//        // but is passed immediately as an expression. A lambda expression
//        // is always surrounded by curly braces { }.
//        viewModel.userNameL.observe(viewLifecycleOwner,
//                { name -> binding.txtProfileUsername.text = name
//                })
//
//    }

    fun getUserInfo() {
        //getUserName

        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                //retrieve data to pass to viewmodel factory
                profileUserName = (snapshot.child("username").getValue().toString())
                profileEmail = (snapshot.child("email").getValue().toString())
                profilePhoneNumber = (snapshot.child("phone").getValue().toString())
                profilePicture = (snapshot.child("profilePicture").getValue().toString())
                bankCard = (snapshot.child("cardNumber").getValue().toString())
                //get it loaded only initially
                if (isFirstTime) {
                    binding.txtProfileUsername.text = (snapshot.child("username").getValue().toString())
                    binding.txtProfileEmail.text = (snapshot.child("email").getValue().toString())
                    binding.txtPhoneNumber.text = (snapshot.child("phone").getValue().toString())
                    binding.txtBankCard.text = (snapshot.child("cardNumber").getValue().toString())
                    getActivity()?.let {
                        Glide.with(it)
                                .load(snapshot.child("profilePicture").getValue().toString())
                                .into(binding.ProfileImage)
                    }

                    isFirstTime = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    fun showProgressDialog(text: String){

        getActivity()?.let {
            progressDialog = Dialog(it)
        }

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
            refreshPhoneNumber()
    }


    fun refreshPhoneNumber(){
        //getUserName
        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                binding.txtPhoneNumber.text = (snapshot.child("phone").getValue().toString())
                binding.txtBankCard.text = (snapshot.child("cardNumber").getValue().toString())

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        showProgressDialog("Updating...")
        // pass to viewmodelfactory to let it return to viewmodel
        val viewModelFactory = ProfileViewModelFactory(
                profileUserName,
                profileEmail,
                profilePhoneNumber,
                profilePicture,
                bankCard
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        //take data from viewmodel
        binding.txtProfileUsername.text = viewModel.profileUserName
        binding.txtProfileEmail.text = viewModel.profileEmail
        binding.txtBankCard.text = viewModel.cardNumber
//        binding.txtPhoneNumber.text = viewModel.profilePhoneNumber

        viewModel.profilePhoneNumber.observe(viewLifecycleOwner,
                { phone ->
                    binding.txtPhoneNumber.text = phone
                })

        if(isEditProfilePicture){
            if(!viewModel.profilePicture.isEmpty()){
                getActivity()?.let {
                    Glide.with(it)
                            .load(selectedImages)
                            .into(binding.ProfileImage)
                }
            }
            isEditProfilePicture = false
        } else if(!viewModel.profilePicture.isEmpty()){
            getActivity()?.let {
                Glide.with(it)
                        .load(viewModel.profilePicture)
                        .into(binding.ProfileImage)
            }
        }
    }

    // call after photo selected
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null){
            // proceed and check whatthe selected image was
            isEditProfilePicture = true

            filepath = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(this.requireActivity().contentResolver, filepath)
            ProfileImage.setImageBitmap(bitmap)

            upload()

        }
    }




    fun upload(){
        showProgressDialog("Updating Image...")
        val filename = UUID.randomUUID().toString()
        val ref1 = FirebaseStorage.getInstance().getReference("/profilePicture/$filename")
        if(filepath == null) return
        ref1.putFile(filepath!!)
            .addOnSuccessListener {

                ref1.downloadUrl.addOnSuccessListener {
                    selectedImages =  it.toString()

                    val ref = FirebaseDatabase.getInstance().getReference("users")

                    ref.child(userId).child("profilePicture").setValue(selectedImages.toString())


                    hideProgressDialog()

                    Toast.makeText(getActivity()?.applicationContext, "Successfully uploaded", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener{
            }
        return
    }
}