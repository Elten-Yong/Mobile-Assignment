package com.example.androidassignment.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.androidassignment.R
import com.example.androidassignment.databinding.SearchingActivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_posts2.view.*

class SearchingActivity : AppCompatActivity() {

    lateinit var binding: SearchingActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searching_activity)
        val actionBar = supportActionBar

        actionBar!!.title = "Search"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding = SearchingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = GroupAdapter<GroupieViewHolder>()

        binding.recyclerViewSearching.adapter = adapter

        //Searching when text entered
        binding.SearchBar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!binding.SearchBar.text.isEmpty()){
                    fetchPostData(s.toString())
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    //Back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object{
        val POST_KEY = "POST_KEY"
    }

    //Load data
    private fun fetchPostData(s: String){
        val ref = FirebaseDatabase.getInstance().getReference("user post").orderByChild("topic").startAt(s).endAt(s+"\uf8ff")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach{
                    Log.d("posting", it.toString())

                    val post= it.getValue(UserPost::class.java)
                    if(post != null){

                        adapter.add(PostItem(post))

                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val postItem = item as PostItem
                    val intent= Intent(view.context, ViewPostActivity::class.java)
                    intent.putExtra(POST_KEY, postItem.post)
                    startActivity(intent)
                    finish()
                }
                binding.recyclerViewSearching.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    //Add data to recycler view
    class PostItem(val post:UserPost): Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.topic.text = post.topic
            Picasso.get().load(post.photoUpload).into(viewHolder.itemView.postPic)
        }

        override fun getLayout(): Int {
            return R.layout.user_posts
        }
    }
}