package com.example.androidassignment.Community

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.Community.CommunityActivity.Companion.POST_KEY
import com.example.androidassignment.R
import com.example.androidassignment.databinding.ManagePostActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.manage_post_activity.*
import java.util.*

class ManagePostActivity : AppCompatActivity() {

    lateinit var binding: ManagePostActivityBinding
    lateinit var imageUrl: String
    private var filepath: Uri? = null
    private var photo: String? = null
    private var changePic: Int = 0
    lateinit var postID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_post_activity)
        val actionBar = supportActionBar

        actionBar!!.title = "Edit post"
        actionBar.setDisplayHomeAsUpEnabled(true)


        binding = ManagePostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent();

        val post = intent.getParcelableExtra<UserPost>(POST_KEY)

        if (post != null) {
            postID = post.postID
            imageUrl = post.photoUpload
            Picasso.get().load(post.photoUpload).into(binding.postPic)
            binding.topic.setText(post.topic)
            binding.description.setText(post.description)
        }

        binding.Submit.setOnClickListener {
            if(changePic != 0) {
                update()

            }else {
                updateNoPic()
            }

            if(binding.topic.text!= null && binding.description.text != null){
                finish()
            }

        }

        binding.Delete.setOnClickListener {
            deletePost()
        }

        binding.Upload.setOnClickListener{
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
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            postPic.setImageBitmap(bitmap)
            changePic++
        }
    }

    private fun update(){
        val topic = binding.topic
        val text = binding.description

        if(topic.text.isEmpty()){
            topic.error = "Please enter the topic"
            topic.requestFocus()
            return
        }

        if(text.text.isEmpty()){
            text.error = "Please enter the description"
            text.requestFocus()
            return
        }

        if(postPic.drawable ==null){
            Toast.makeText(applicationContext, "Upload your image first", Toast.LENGTH_LONG).show()
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/user's post/$filename")
        if(filepath == null) return
        ref.putFile(filepath!!)
            .addOnSuccessListener {
                Log.d("upload", "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    photo =  it.toString()
                    Log.d("upload", "File Location:$it")
                    val userID = FirebaseAuth.getInstance().currentUser!!.uid
                    val ref1 = FirebaseDatabase.getInstance().getReference("user post")
                    val postID = postID
                    val post = UserPost(topic.text.toString(), text.text.toString(), photo.toString(), userID.toString(), postID.toString())

                    ref1.child(postID.toString()).setValue(post)

                    Toast.makeText(applicationContext, "Succesfully uploaded", Toast.LENGTH_LONG).show()
                    val intent= Intent(this, ManagePost::class.java)
                    this.startActivity(intent)
                }
            }
            .addOnFailureListener{
            }
        return
    }

    private fun updateNoPic() {
        val topic = binding.topic
        val text = binding.description

        if(topic.text.isEmpty()){
            topic.error = "Please enter your subject"
            topic.requestFocus()
            return
        }

        if(text.text.isEmpty()){
            text.error = "Please enter your subject"
            text.requestFocus()
            return
        }

        if(postPic.drawable ==null){
            Toast.makeText(applicationContext, "Upload your image first", Toast.LENGTH_LONG).show()
        }

        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("user post")
        val postID = postID
        val post = UserPost(topic.text.toString(), text.text.toString(), imageUrl, userID.toString(), postID.toString())

        ref.child(postID.toString()).setValue(post)

        Toast.makeText(applicationContext, "Succesfully uploaded", Toast.LENGTH_LONG).show()
        val intent= Intent(this, ManagePost::class.java)
        this.startActivity(intent)
    }

    private fun deletePost(){
        val ref = FirebaseDatabase.getInstance().getReference("user post").child(postID)
        ref.removeValue()
        Toast.makeText(applicationContext, "Post deleted", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}