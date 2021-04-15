package com.example.androidassignment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    private var _binding: CommunityFragmentBinding? = null
    private val binding get() = _binding!!
    //private val TAG = "ReadAndWriteSnippets"

    //private lateinit var viewModel: CommunityViewModel

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

    companion object{
        const val POST_KEY = "POST_KEY"
        //fun newInstance() = CommunityActivity()
    }

    private fun fetchPostData(){
        val ref = FirebaseDatabase.getInstance().getReference("user post")
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
                    val intent= Intent(view.context, ManagePostActivity::class.java)
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

