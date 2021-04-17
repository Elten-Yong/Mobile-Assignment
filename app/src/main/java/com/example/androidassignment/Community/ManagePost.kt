package com.example.androidassignment.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.androidassignment.R
import com.example.androidassignment.databinding.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.manage_post.*
import kotlinx.android.synthetic.main.user_posts2.view.*

class ManagePost : AppCompatActivity() {

    lateinit var binding: ManagePostBinding
    lateinit var postID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.manage_post)

        supportActionBar?.title = "View your posts"

        binding = ManagePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = GroupAdapter<GroupieViewHolder>()

        recycler_view_manage_post.adapter = adapter

        //Back button
        binding.toolbarPost.setNavigationOnClickListener {
            finish()
        }

        //Refresh function
        binding.swipeRefresh.setOnRefreshListener {
            binding.recyclerViewManagePost.adapter = adapter
            fetchData()
            Toast.makeText(applicationContext, "Updated!", Toast.LENGTH_SHORT).show();
            binding.swipeRefresh.isRefreshing = false
        }

        fetchData()
    }

    companion object{
        val POST_KEY = "POST_KEY"
    }

    //Load data function
    private fun fetchData(){
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("user post")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach{
                    Log.d("loading", it.toString())
                    val post= it.getValue(UserPost::class.java)
                    if(post!!.userID == userID){
                        adapter.add(PostItem(post))
                    }
                }
                //Click on picture
                adapter.setOnItemClickListener { item, view ->
                    val postItem = item as PostItem
                    val intent= Intent(view.context, ManagePostActivity::class.java)
                    intent.putExtra(POST_KEY, postItem.post)
                    startActivity(intent)
                }

                recycler_view_manage_post.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}

//Add data to recycler view function
class PostItem(val post: UserPost): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.topic.text = post.topic
        viewHolder.itemView.description.text = post.description
        Picasso.get().load(post.photoUpload).into(viewHolder.itemView.postPic)
    }

    override fun getLayout(): Int {
        return R.layout.user_posts2
    }
}