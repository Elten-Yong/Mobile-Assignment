package com.example.androidassignment

import kotlinx.android.synthetic.main.item_view.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.androidassignment.databinding.FragmentAdminInfoCenterBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_admin_info_center.*
import kotlinx.android.synthetic.main.item_view.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [admin_info_center.newInstance] factory method to
 * create an instance of this fragment.
 */

class admin_info_center : Fragment() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }


    }

    private var _binding: FragmentAdminInfoCenterBinding? = null
    private val binding get() = _binding!!
    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminInfoCenterBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.addPost.setOnClickListener{
            val intent= Intent(getActivity(), addPostActivity::class.java)
            getActivity()?.startActivity(intent)

          binding.recyclerViewAdminInfo.adapter!!.notifyDataSetChanged()
        }

        binding.searchBar.setOnClickListener{
            val intent= Intent(getActivity(), PostSearchingActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        binding.searchIcon.setOnClickListener{
            val intent= Intent(getActivity(), PostSearchingActivity::class.java)
            getActivity()?.startActivity(intent)

        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.recyclerViewAdminInfo.adapter = adapter
            fetchPostData()
            Toast.makeText(getActivity(),"Updated!",Toast.LENGTH_SHORT).show();
            binding.swipeRefresh.isRefreshing = false
        }



        binding.recyclerViewAdminInfo.adapter = adapter
        fetchPostData()


        return view

    }

    companion object{
        val POST_KEY = "POST_KEY"
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerViewAdminInfo.adapter = adapter
        fetchPostData()
    }

    private fun fetchPostData(){
        val ref = FirebaseDatabase.getInstance().getReference("post")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

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
                    getActivity()?.startActivity(intent)
                }
                binding.recyclerViewAdminInfo.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

class PostItem(val post:information): Item<GroupieViewHolder>(){

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