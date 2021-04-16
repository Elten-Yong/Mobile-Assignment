package com.example.androidassignment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidassignment.databinding.FragmentAdminInfoCenterBinding
import com.example.androidassignment.databinding.InfoCenterFragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_view.view.*

class infoCenter : Fragment() {

    private var _binding: InfoCenterFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: InfoCenterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = InfoCenterFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.searchBar.setOnClickListener {
            val intent = Intent(getActivity(), PostSearchingActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        binding.searchIcon.setOnClickListener {
            val intent = Intent(getActivity(), PostSearchingActivity::class.java)
            getActivity()?.startActivity(intent)


        }

        val adapter = GroupAdapter<GroupieViewHolder>()

        binding.swipeRefresh.setOnRefreshListener {
            binding.recyclerViewAdminInfo.adapter = adapter
            fetchPostData()
            Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
            binding.swipeRefresh.isRefreshing = false
        }

        binding.recyclerViewAdminInfo.adapter = adapter
        fetchPostData()


        return view
    }

    companion object {
        val POST_KEY = "POST_KEY"
    }

    private fun fetchPostData() {
        val ref = FirebaseDatabase.getInstance().getReference("post")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach {
                    Log.d("posting", it.toString())
                    val post = it.getValue(information::class.java)
                    if (post != null) {
                        adapter.add(PostItem(post))
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val postItem = item as PostItem
                    val intent = Intent(view.context, PostLayoutActivity::class.java)
                    intent.putExtra(POST_KEY, postItem.post)
                    getActivity()?.startActivity(intent)
                }
                binding.recyclerViewAdminInfo.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    class PostItem(val post: information) : Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.subjectList.text = post.subject
            Picasso.get().load(post.photoUpload).into(viewHolder.itemView.postImage)
        }

        override fun getLayout(): Int {
            return R.layout.item_view
        }

    }
}