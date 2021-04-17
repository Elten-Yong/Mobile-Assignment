package com.example.androidassignment.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.androidassignment.R
import com.example.androidassignment.databinding.ViewPostActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_post_layout.toolbar_post
import kotlinx.android.synthetic.main.comment_activity.view.*
import kotlinx.android.synthetic.main.view_post_activity.*
import java.text.SimpleDateFormat
import java.util.*

class ViewPostActivity : AppCompatActivity() {

    lateinit var binding: ViewPostActivityBinding
    lateinit var imageUrl: String
    lateinit var postID: String
    var postTopic: String? = null
    var fontsize: Float = 10F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_post_activity)

        val userID = FirebaseAuth.getInstance().currentUser!!.uid

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

        //Back function
        binding.toolbarPost.setNavigationOnClickListener {
            finish()
        }

        toolbar_post.setOnMenuItemClickListener() {
            when(it.itemId){

                R.id.iconShare -> share()
                R.id.iconFontSetting -> {
                    fontsize()
                    binding.Description.textSize= fontsize
                    binding.Topic.textSize = fontsize
                }
            }
            false
        }

        val adapter = GroupAdapter<GroupieViewHolder>()

        //Refresh function
        binding.swipeRefresh.setOnRefreshListener {
            binding.recyclerViewComments.adapter = adapter
            fetchUser()
            Toast.makeText(applicationContext, "Updated!", Toast.LENGTH_SHORT).show();
            binding.swipeRefresh.isRefreshing = false
        }

        //Add button
        binding.Add.setOnClickListener {
            addComment()
        }

        //Donate button
        binding.donateBtn.setOnClickListener {

        }

        recycler_view_comments.adapter = adapter

        fetchUser()
    }

    //Share function
    private fun share(){
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, postTopic)
        intent.type = "text/plain"

        startActivity(Intent.createChooser(intent, "Share to : "))
    }

    //Change font function
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

    //Back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //Add comment function
    private fun addComment(){
        val comment = binding.edittxtComment
        val com = comment.text.toString()
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        if(comment.text.isEmpty()){
            comment.error = "Please enter the topic"
            comment.requestFocus()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("user post").child(postID).child("post comments")
        val ref1= FirebaseDatabase.getInstance().getReference("users").child(userID)
        ref1.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val userName = (snapshot.child("username").value.toString())
                val profilePic = (snapshot.child("profilePicture").value.toString())
                val sdf = SimpleDateFormat("hh:mm    dd/M/yyyy")
                val currentDate =sdf.format(Date())

                //val ref2 = Comment(userName, com, profilePic, Calendar.getInstance().getTime().toString())
                val ref2 = Comment(userName, com, profilePic, currentDate.toString())
                val key = ref.push().key
                ref.child(key.toString()).setValue(ref2)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        comment.setText("")

        Toast.makeText(applicationContext, "Succesfully added a comment", Toast.LENGTH_LONG).show()
    }

    //Load data
    private fun fetchUser(){
        val ref = FirebaseDatabase.getInstance().getReference("user post").child(postID).child("post comments")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                    snapshot.children.forEach {
                        Log.d("comment", it.toString())
                        val comment = it.getValue(Comment::class.java)
                        if (comment != null) {
                            adapter.add(CommentItem(comment))
                        }
                     }

                binding.recyclerViewComments.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    //Add data to recycler view
    class CommentItem(private val comment: Comment): Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.Username.text = comment.username
            viewHolder.itemView.Text.text = comment.text
            viewHolder.itemView.Date.text = comment.time
            Picasso.get().load(comment.profilePic).into(viewHolder.itemView.ProfilePic)
        }

        override fun getLayout(): Int {
            return R.layout.comment_activity
        }
    }
}

