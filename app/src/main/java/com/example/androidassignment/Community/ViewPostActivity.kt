package com.example.androidassignment.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.androidassignment.R
import com.example.androidassignment.databinding.ViewPostActivityBinding
import com.example.androidassignment.information
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_layout.*

class ViewPostActivity : AppCompatActivity() {


    lateinit var binding: ViewPostActivityBinding
    var fontsize: Float = 10F
    lateinit var imageUrl: String
    lateinit var postID: String
    var postTopic: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_post_activity)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        binding = ViewPostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent();
        val post = intent.getParcelableExtra<UserPost>(CommunityActivity.POST_KEY)
        if (post != null) {
            postTopic = post.topic
            postID = post.postID
            imageUrl = post.photoUpload
            Picasso.get().load(post.photoUpload).into(binding.Picture)
            binding.Topic.setText(post.topic)
            binding.Description.setText(post.description)
        }

        binding.toolbarPost.setNavigationOnClickListener {
            finish()
        }

        toolbar_post.setOnMenuItemClickListener() {
            when(it.itemId){
                R.id.iconBookmark -> {
                    favourite(userId, post!!)
                    Toast.makeText(applicationContext, "bookmark", Toast.LENGTH_LONG).show()
                }
                R.id.iconShare -> share()
                R.id.iconFontSetting -> {
                    fontsize()
                    binding.Description.textSize= fontsize
                    binding.Topic.textSize = fontsize
                }
            }
            false
        }
    }

    private fun share(){
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, postTopic)
        intent.type = "text/plain"

        startActivity(Intent.createChooser(intent, "Share to : "))
    }

    private fun fontsize(): Float{
       if(fontsize == 10F){
           fontsize = 20F
           return fontsize
       }else if(fontsize == 20F){
           fontsize = 30F
           return fontsize
       }else{
           fontsize = 10F
           return fontsize
       }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun favourite(userId: String, post: UserPost){
        val ref = FirebaseDatabase.getInstance().getReference("favouriteList")
        val ref_check = FirebaseDatabase.getInstance().getReference("favouriteList").child(userId).child("postID")
        ref_check.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var added = false
                snapshot.children.forEach {
                    Log.d("posting", it.toString())

                    val postForChecking = it.getValue(information::class.java)

                    if(postForChecking!!.postID.equals(post.postID)){
                        added = true
                        FirebaseDatabase.getInstance().getReference("favouriteList").child(userId).child(post.postID).removeValue()
                    }

                }
                if(!added) ref.child(userId).child(post.postID).setValue(post)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}