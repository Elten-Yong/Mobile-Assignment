package com.example.androidassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.androidassignment.databinding.ActivityEditPostBinding
import com.example.androidassignment.databinding.ActivityPostSearchingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_post_searching.*
import kotlinx.android.synthetic.main.item_view.view.*

class PostSearchingActivity : AppCompatActivity() {

    lateinit var binding: ActivityPostSearchingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_searching)
        val actionBar = supportActionBar

        actionBar!!.title = "Search"
        actionBar.setDisplayHomeAsUpEnabled(true)


        binding = ActivityPostSearchingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = GroupAdapter<GroupieViewHolder>()

        binding.recyclerViewSearching.adapter = adapter
        binding.searchBar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!binding.searchBar.text.isEmpty()){
                    fetchPostData(s.toString())
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object{
        val POST_KEY = "POST_KEY"
    }

    private fun fetchPostData(s: String){
        val ref = FirebaseDatabase.getInstance().getReference("post").orderByChild("subject").startAt(s).endAt(s+"\uf8ff")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach{
                    Log.d("posting", it.toString())

                    val post= it.getValue(information::class.java)
                    if(post != null){

                        adapter.add(PostItem(post))

                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val postItem = item as PostItem
                    val intent= Intent(view.context, EditPostActivity::class.java)
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

    class PostItem(val post:information): Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.subjectList.text = post.subject
            Picasso.get().load(post.photoUpload).into(viewHolder.itemView.postImage)
        }

        override fun getLayout(): Int {
            return R.layout.item_view
        }
    }
}