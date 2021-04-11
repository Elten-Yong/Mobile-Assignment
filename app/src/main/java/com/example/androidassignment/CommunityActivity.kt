package com.example.androidassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidassignment.databinding.CommunityFragmentBinding
import android.content.Intent
import android.widget.Button
import com.example.androidassignment.databinding.FragmentAdminInfoCenterBinding

class CommunityActivity : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    private var _binding: CommunityFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = CommunityActivity()
    }

    private lateinit var viewModel: CommunityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = CommunityFragmentBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.WritePost.setOnClickListener{
            val intent= Intent(getActivity(), CreatePostActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)
        // TODO: Use the ViewModel
    }
}

