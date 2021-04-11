package com.example.androidassignment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.AddPostActivityBinding
import kotlinx.android.synthetic.main.add_post_activity.*
import java.net.URL

class addPostActivity : AppCompatActivity() {

    lateinit var binding: AddPostActivityBinding
    lateinit var filepath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_post_activity)

        binding = AddPostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submit.setOnClickListener {
            submitPost()

        }

        binding.cancelAction.setOnClickListener {

        }

        binding.upload.setOnClickListener{
            choosePic();
        }

    }

    private fun submitPost(){

    }

    private fun choosePic(){
        var image = Intent()
        image.setType("image/*")
        image.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(image, "Choose Picture"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null){
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            imageView.setImageBitmap(bitmap)
        }
    }
}