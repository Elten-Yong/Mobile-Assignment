package com.example.androidassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidassignment.databinding.CommunityFragmentBinding

class CommunityActivity : Fragment() {

    private var _binding: CommunityFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = CommunityActivity()
    }

    private lateinit var viewModel: CommunityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.community_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)
        // TODO: Use the ViewModel
    }

}