package com.example.androidassignment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.databinding.ActivityEditPostBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_post.*
import java.util.*


class EditPostActivity : AppCompatActivity() {



    lateinit var binding: ActivityEditPostBinding
    var filepath: Uri? = null
    var SelectedImages: String? = null
    lateinit var postId: String
    lateinit var time: String
    var totalVisitor: Int = 0
    lateinit var imageUrl: String
    var changedOnphoto: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)
        val actionBar = supportActionBar

        actionBar!!.title = "Edit Post"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()


        val post = intent.getParcelableExtra<information>(admin_info_center.POST_KEY)


        if (post != null) {
            time = post.time
            totalVisitor = post.totalVisitor.toInt()
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

        }

        binding.deleteAction.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete")
            builder.setMessage("Are your sure you want to delete this information post?")
            builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int -> deletePost() }
            builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
            builder.show()

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

        if(subject.text.isEmpty() || subject.text.length > 75){

            if(subject.text.length > 75){
                subject.error = "Do not more than 75 words"
                subject.requestFocus()
                return
            }else{
                subject.error = "Please enter your subject"
                subject.requestFocus()
                return
            }

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

        ref1.putFile(filepath!!)
            .addOnSuccessListener {
                Log.d("upload", "Successfully uploaded image: ${it.metadata?.path}")
                ref1.downloadUrl.addOnSuccessListener {
                    SelectedImages =  it.toString()
                    Log.d("upload", "File Location:$it")
                    val ref = FirebaseDatabase.getInstance().getReference("post")
                    val postID = postId
                    val post = information(postID.toString(), subject.text.toString(), text.text.toString(), SelectedImages.toString(), time, totalVisitor.toString())
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
        val post = information(postID.toString(), subject.text.toString(), text.text.toString(), imageUrl, time, totalVisitor.toString())
        ref.child(postID).setValue(post)
        Toast.makeText(applicationContext, "Succesfully updated", Toast.LENGTH_LONG).show()
    }


}
