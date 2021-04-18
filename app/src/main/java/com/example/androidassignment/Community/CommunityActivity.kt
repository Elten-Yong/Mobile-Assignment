package com.example.androidassignment.Community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.androidassignment.*
import com.example.androidassignment.databinding.CommunityFragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_posts.view.*

class CommunityActivity : Fragment() {

    private var _binding: CommunityFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        _binding = CommunityFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        //Add post button
        binding.WritePost.setOnClickListener{
            val intent= Intent(getActivity(), CreatePostActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        //Edit post button
        binding.EditPost.setOnClickListener{
            val intent= Intent(getActivity(), ManagePost::class.java)
            getActivity()?.startActivity(intent)
        }

        //Search bar
        binding.SearchBar.setOnClickListener{
            val intent= Intent(getActivity(), SearchingActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        //Search icon
        binding.SearchIcon.setOnClickListener{
            val intent= Intent(getActivity(), SearchingActivity::class.java)
            getActivity()?.startActivity(intent)


        }

        val adapter = GroupAdapter<GroupieViewHolder>()

        //Refresh function
        binding.swipeRefresh.setOnRefreshListener {
            binding.recyclerViewUserPost.adapter = adapter
            fetchPostData()
            Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
            binding.swipeRefresh.isRefreshing = false
        }

        binding.recyclerViewUserPost.adapter = adapter
        fetchPostData()

        return view
    }

    companion object{
        const val POST_KEY = "POST_KEY"
        //fun newInstance() = CommunityActivity()
    }

    //Load data function
    private fun fetchPostData(){
        val ref = FirebaseDatabase.getInstance().getReference("/user post")
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
                    getActivity()?.startActivity(intent)
                }
                binding.recyclerViewUserPost.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    //Add data to recycler view function
    class PostItem(val post: UserPost): Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.topic.text = post.topic
            Picasso.get().load(post.photoUpload).into(viewHolder.itemView.postPic)
        }

        override fun getLayout(): Int {
            return R.layout.user_posts
        }
    }

}

