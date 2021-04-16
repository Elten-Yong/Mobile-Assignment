package com.example.androidassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.androidassignment.databinding.ActivityBookmarkBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_view.view.*

class BookmarkActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookmarkBinding
    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
        val actionBar = supportActionBar

        actionBar!!.title = "Bookmark"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = GroupAdapter<GroupieViewHolder>()
        fetchPostData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        val POST_KEY = "POST_KEY"
    }

    override fun onResume() {
        super.onResume()
        fetchPostData()
    }

    private fun fetchPostData() {
        val ref = FirebaseDatabase.getInstance().getReference("post")
       val ref1 =  FirebaseDatabase.getInstance().getReference("favouriteList").child(userId)

        ref1.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot_favourite: DataSnapshot) {

                ref.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val adapter = GroupAdapter<GroupieViewHolder>()

                        snapshot.children.forEach {
                            Log.d("posting", it.toString())
                            val post = it.getValue(information::class.java)
                            if (post != null) {
                                if (snapshot_favourite.hasChild(post.postID)) {
                                    adapter.add(PostItem(post))
                                }
                            }
                        }

                        adapter.setOnItemClickListener { item, view ->
                            val postItem = item as PostItem
                            val intent= Intent(view.context, ViewBookmarkPostActivity::class.java)
                            intent.putExtra(POST_KEY, postItem.post)
                            startActivity(intent)
                        }
                        binding.recyclerViewBookmarkInfo.adapter = adapter
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    class PostItem(val post: information) : Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.totalVisitor.text = post.totalVisitor
            viewHolder.itemView.textDesc.text = post.contentPost.take(20)
            viewHolder.itemView.subjectList.text = post.subject
            Picasso.get().load(post.photoUpload).into(viewHolder.itemView.postImage)
        }

        override fun getLayout(): Int {
            return R.layout.item_view
        }

    }
}