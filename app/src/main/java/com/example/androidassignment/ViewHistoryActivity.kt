package com.example.androidassignment

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.androidassignment.databinding.ActivityViewHistoryBinding
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

class ViewHistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityViewHistoryBinding
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_history)
        val actionBar = supportActionBar

        actionBar!!.title = "History"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding = ActivityViewHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = GroupAdapter<GroupieViewHolder>()
        fetchPostData()

        binding.clearHistory.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Clean history")
            builder.setMessage("Are your sure you want to clean your history?")
            builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int -> cleanHistory() }
            builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
            builder.show()
        }
    }

    private fun cleanHistory(){
        FirebaseDatabase.getInstance().getReference("historyList").child(userId).removeValue()
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
        val ref1 =  FirebaseDatabase.getInstance().getReference("historyList").child(userId)

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
                            val intent= Intent(view.context, ViewHistoryPostActivity::class.java)
                            intent.putExtra(POST_KEY, postItem.post)
                            startActivity(intent)
                        }
                        binding.recyclerViewHistoryInfo.adapter = adapter
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