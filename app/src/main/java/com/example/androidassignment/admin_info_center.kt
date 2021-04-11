package com.example.androidassignment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.androidassignment.databinding.FragmentAdminInfoCenterBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [admin_info_center.newInstance] factory method to
 * create an instance of this fragment.
 */

class admin_info_center : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    private var _binding: FragmentAdminInfoCenterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminInfoCenterBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.addPost.setOnClickListener{
            val intent= Intent(getActivity(), addPostActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        return view


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}