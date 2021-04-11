package com.example.androidassignment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.AddPostActivityBinding
import com.example.androidassignment.databinding.CreatePostFragmentBinding
import kotlinx.android.synthetic.main.add_post_activity.*

class CreatePostActivity : AppCompatActivity() {

    lateinit var binding: CreatePostFragmentBinding
    lateinit var filepath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post_fragment)

        binding = CreatePostFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Submit.setOnClickListener {
            submitPost()

        }

        binding.Cancel.setOnClickListener {

        }

        binding.Upload.setOnClickListener{
            choosePic();
        }

    }

    private fun submitPost(){
        val subject = binding.TopicInput
        val text = binding.DescriptionInput

        if(subject.text.isEmpty()){
            subject.error = "Please enter the topic"
            subject.requestFocus()
            return
        }

        if(text.text.isEmpty()){
            text.error = "Please enter the description"
            text.requestFocus()
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
}