package com.example.androidassignment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.AddPostActivityBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.add_post_activity.*
import java.util.*

class addPostActivity : AppCompatActivity() {

    lateinit var binding: AddPostActivityBinding
    var SelectedImages: String? = null
    var filepath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_post_activity)
        supportActionBar?.title = "Add Post"

        binding = AddPostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submit.setOnClickListener {
            submitPost()
            finish()
        }

        binding.upload.setOnClickListener{
            choosePic();
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
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            imageView.setImageBitmap(bitmap)
        }
    }



    private fun submitPost(){
        val subject = binding.subjectList
        val text = binding.userPost

        if(subject.text.isEmpty()){
            subject.error = "Please enter your subject"
            subject.requestFocus()
            return
        }

        if(text.text.isEmpty()){
            text.error = "Please enter your subject"
            text.requestFocus()
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref1 = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref1.putFile(filepath!!)
                .addOnSuccessListener {
                    Log.d("upload", "Successfully uploaded image: ${it.metadata?.path}")
                    ref1.downloadUrl.addOnSuccessListener {
                        SelectedImages =  it.toString()
                       Log.d("upload", "File Location:$it")
                        val ref = FirebaseDatabase.getInstance().getReference("post")
                        val postID = ref.push().key
                        val post = information(postID.toString(), subject.text.toString(), text.text.toString(), SelectedImages.toString())

                        ref.child(postID.toString()).setValue(post)
                        Toast.makeText(applicationContext, "Succesfully uploaded", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener{
                }
        return
    }

}
