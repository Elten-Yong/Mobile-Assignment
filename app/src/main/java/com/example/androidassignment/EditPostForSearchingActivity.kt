package com.example.androidassignment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.ActivityEditPostBinding
import com.example.androidassignment.databinding.ActivityEditPostForSearchingBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_post.*
import java.util.*

class EditPostForSearchingActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditPostForSearchingBinding
    var filepath: Uri? = null
    var SelectedImages: String? = null
    lateinit var postId: String
    lateinit var imageUrl: String
    var changedOnphoto: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post_for_searching)
        setSupportActionBar(findViewById(R.id.toolbar))
        val actionBar = supportActionBar

        actionBar!!.title = "Edit Post"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding = ActivityEditPostForSearchingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent();
        val activity = intent.getStringExtra("view.context")
        Log.d("Testing", "From the last slide: $activity")

        val post = intent.getParcelableExtra<information>(PostSearchingActivity.POST_KEY)


        if (post != null) {
            postId = post.postID
            imageUrl = post.photoUpload
            Picasso.get().load(post.photoUpload).into(binding.imageView)
            binding.subjectList.setText(post.subject)
            binding.userPost.setText(post.contentPost)
        }

        binding.submit.setOnClickListener{
            if(changedOnphoto != 0) {

                updateDatabase()
            }else {
                updateDatabaseWithoutPhoto()
            }

            if(binding.subjectList.text!= null && binding.userPost.text != null){
                finish()
            }
        }

        binding.upload.setOnClickListener{
            choosePic()
            //post!!.subject
        }

        binding.deleteAction.setOnClickListener{
            deletePost()
        }

    }

    private fun deletePost(){
        val ref = FirebaseDatabase.getInstance().getReference("post").child(postId)
        ref.removeValue()
        Toast.makeText(applicationContext, "Post deleted", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
            changedOnphoto++
        }
    }

    private fun updateDatabase(){
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

        if(imageView.getDrawable()==null){
            Toast.makeText(applicationContext, "Upload your image first", Toast.LENGTH_LONG).show()
        }
        val filename = UUID.randomUUID().toString()
        val ref1 = FirebaseStorage.getInstance().getReference("/images/$filename")
        if(filepath == null) return
        {
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

        }
        ref1.putFile(filepath!!)
            .addOnSuccessListener {
                Log.d("upload", "Successfully uploaded image: ${it.metadata?.path}")
                ref1.downloadUrl.addOnSuccessListener {
                    SelectedImages =  it.toString()
                    Log.d("upload", "File Location:$it")
                    val ref = FirebaseDatabase.getInstance().getReference("post")
                    val postID = postId
                    val post = information(postID.toString(), subject.text.toString(), text.text.toString(), SelectedImages.toString())
                    ref.child(postID.toString()).setValue(post)
                    Toast.makeText(applicationContext, "Succesfully uploaded", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener{
            }
        return
    }

    private fun updateDatabaseWithoutPhoto() {
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

        if(imageView.getDrawable()==null){
            Toast.makeText(applicationContext, "Upload your image first", Toast.LENGTH_LONG).show()
        }
        val ref = FirebaseDatabase.getInstance().getReference("post")
        val postID = postId
        val post = information(postID.toString(), subject.text.toString(), text.text.toString(), imageUrl)
        ref.child(postID).setValue(post)
        Toast.makeText(applicationContext, "Succesfully updated", Toast.LENGTH_LONG).show()
    }

}