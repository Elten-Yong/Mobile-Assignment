package com.example.androidassignment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.CreatePostFragmentBinding
import com.example.androidassignment.databinding.ManagePostFragmentBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.add_post_activity.*
import java.util.*

class ManagePostActivity : AppCompatActivity() {

    lateinit var binding: ManagePostFragmentBinding
    lateinit var filepath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_post_fragment)

        binding = ManagePostFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Submit.setOnClickListener {
            submitPost()

        }

        binding.Delete.setOnClickListener {

        }

        binding.Upload.setOnClickListener{
            choosePic();
        }
    }

    private fun submitPost(){
        val topic = binding.Topic
        val description = binding.Description

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
    }

    private fun choosePic(){
        val image = Intent()
        image.setType("image/*")
        image.setAction(Intent.ACTION_GET_CONTENT)
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

    private fun uploadImageToFirebaseStorage(){
        if (filepath == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(filepath)
    }
}