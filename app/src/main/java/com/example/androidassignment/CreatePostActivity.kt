package com.example.androidassignment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.CreatePostActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.add_post_activity.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CreatePostActivity() : AppCompatActivity() {

    lateinit var binding: CreatePostActivityBinding
    lateinit var filepath: Uri

    private lateinit var auth: FirebaseAuth // Authentication
    private lateinit var database: DatabaseReference //Reference

    var photo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post_activity)

        auth = Firebase.auth //initialise firebase auth object

        binding = CreatePostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Submit.setOnClickListener {
            addPost()
        }

        binding.Cancel.setOnClickListener {
            super.onBackPressed()
        }

        binding.Upload.setOnClickListener{
            selectPhoto();
        }

    }

    private fun addPost(){
        val topic = binding.TopicInput
        val description = binding.DescriptionInput
        /*val pic = binding.imageView

        if(pic.drawable == null){
            pic.requestFocus()
            return
        }*/

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
                //database = Firebase.database.reference // reference to database

                val userID = FirebaseAuth.getInstance().currentUser.uid
                val ref1 = FirebaseDatabase.getInstance().getReference("users/$userID")
                val postId = ref1.push().key
                val post = UserPost(topic.text.toString(), description.text.toString(), photo.toString())

                ref1.child("posts").child(postId.toString()).setValue(post)

                Toast.makeText(applicationContext, "Succesfully uploaded", Toast.LENGTH_LONG).show()
                finish()
            }
        }.addOnFailureListener {}

        return
    }

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