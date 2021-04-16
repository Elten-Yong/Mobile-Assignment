package com.example.androidassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidassignment.databinding.ActivityPostLayoutForUserBinding
import com.example.androidassignment.databinding.ActivityViewBookmarkPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_layout.*

class ViewBookmarkPostActivity : AppCompatActivity() {

    lateinit var binding: ActivityViewBookmarkPostBinding
    lateinit var postId: String
    var fontsize: Float = 10F
    var postSubject: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_bookmark_post)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        binding = ActivityViewBookmarkPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent();
        val post = intent.getParcelableExtra<information>(BookmarkActivity.POST_KEY)

        if (post != null) {
            postSubject = post.subject
            postId = post.postID
            Picasso.get().load(post.photoUpload).into(binding.imageView2)
            binding.subjectList.text = post.subject
            binding.contentPost.text = post.contentPost
            FirebaseDatabase.getInstance().getReference("historyList").child(userId).child(postId).setValue(postId)
            FirebaseDatabase.getInstance().getReference("post").child(postId).child("totalVisitor").get().addOnSuccessListener{
                var noOfVisit:Int = it.value.toString().toInt()
                noOfVisit++
                FirebaseDatabase.getInstance().getReference("post").child(postId).child("totalVisitor").setValue(noOfVisit.toString())
            }
        }
        binding.toolbarPost.setNavigationOnClickListener {
            finish()
        }

        toolbar_post.setOnMenuItemClickListener() {
            when(it.itemId){
                R.id.iconBookmark -> favourite(userId, post!!)

                R.id.iconShare -> share()
                R.id.iconFontSetting -> {
                    fontsize()
                    binding.contentPost.textSize= fontsize
                    binding.subjectList.textSize = fontsize
                }
            }
            true
        }
    }
    private fun share(){
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, postSubject)
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

    private fun favourite(userId: String, post: information){
        val ref = FirebaseDatabase.getInstance().getReference("favouriteList").child(userId)

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val added = snapshot.hasChild(post.postID)

                if(added){
                    FirebaseDatabase.getInstance().getReference("favouriteList").child(userId).child(post.postID).removeValue()
                    Toast.makeText(applicationContext, "bookmark deleted", Toast.LENGTH_LONG).show()

                }else{
                    ref.child(post.postID).setValue(post.postID)
                    Toast.makeText(applicationContext, "bookmark", Toast.LENGTH_LONG).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}