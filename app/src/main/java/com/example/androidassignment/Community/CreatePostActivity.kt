package com.example.androidassignment.Community

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.add_post_activity.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.example.androidassignment.R
import com.example.androidassignment.databinding.CreatePostActivityBinding
import java.util.*

class CreatePostActivity() : AppCompatActivity() {

    lateinit var binding: CreatePostActivityBinding

    private lateinit var auth: FirebaseAuth // Authentication
    lateinit var filepath: Uri
    var photo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post_activity)
        supportActionBar?.title = "Add a post"

        auth = Firebase.auth //initialise firebase auth object

        binding = CreatePostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Submit button
        binding.Submit.setOnClickListener {
            submitPost()
        }

        //Cancel button
        binding.Cancel.setOnClickListener {
            super.onBackPressed()
        }

        //Upload button
        binding.Upload.setOnClickListener{
            selectPhoto();
        }

    }

    //Submit post function
    private fun submitPost(){
        val topic = binding.TopicInput
        val description = binding.DescriptionInput

        if(imageView.drawable ==null){
            Toast.makeText(applicationContext, "Please select a picture", Toast.LENGTH_LONG).show()
            imageView.requestFocus()
            return
        }

        if(topic.text.isEmpty()){
            topic.error = "Please enter the topic"
            topic.requestFocus()
            return
        }

        if(description.text.isEmpty()){
            description.error = "Please enter the description"
            description.requestFocus()
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/user's post/$filename")
        ref.putFile(filepath).addOnSuccessListener(this) {
            Log.d("upload", "Successfully uploaded image: ${it.metadata?.path}")
            ref.downloadUrl.addOnSuccessListener {
                photo = it.toString()
                Log.d("upload", "File Location:$it")

                val userID = FirebaseAuth.getInstance().currentUser!!.uid
                val ref1 = FirebaseDatabase.getInstance().getReference("/user post")
                val postID = ref1.push().key
                val post = UserPost(topic.text.toString(), description.text.toString(), photo.toString(), userID.toString(), postID.toString())

                ref1.child(postID.toString()).setValue(post)

                Toast.makeText(applicationContext, "Succesfully uploaded", Toast.LENGTH_LONG).show()
                finish()
            }

        }.addOnFailureListener {}

        return
    }

    //Choose photo function
    private fun selectPhoto(){
        val image = Intent()
        image.type = "image/*"
        image.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(image, "Choose Picture"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null){
            filepath = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            imageView.setImageBitmap(bitmap)
        }
    }

}