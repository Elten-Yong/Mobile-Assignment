package com.example.androidassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidassignment.databinding.CommunityFragmentBinding
import android.content.Intent
import android.util.Log
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.create_post_activity.view.*
import kotlinx.android.synthetic.main.user_posts.view.*


class CommunityActivity : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    private var _binding: CommunityFragmentBinding? = null
    private val binding get() = _binding!!

    /*companion object {
        fun newInstance() = CommunityActivity()


    private lateinit var viewModel: CommunityViewModel */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = CommunityFragmentBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.WritePost.setOnClickListener{
            val intent= Intent(getActivity(), CreatePostActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        binding.EditPost.setOnClickListener{
            val intent= Intent(getActivity(), ManagePostActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        val adapter = GroupAdapter<GroupieViewHolder>()

        binding.recyclerViewUserPost.adapter = adapter
        fetchPostData()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

    private fun fetchPostData(){
        val ref = FirebaseDatabase.getInstance().getReference("users")
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

                adapter.setOnItemClickListener { _, view ->
                    val intent= Intent(view.context, ManagePostActivity::class.java)
                    activity?.startActivity(intent)
                }
                binding.recyclerViewUserPost.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    class PostItem(val post:UserPost): Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.TopicEx.text = post.topic
            Picasso.get().load(post.photoUpload).into(viewHolder.itemView.postImage)
        }

        override fun getLayout(): Int {
            return R.layout.user_posts
        }
    }
}

