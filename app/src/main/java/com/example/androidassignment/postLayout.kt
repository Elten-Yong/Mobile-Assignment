package com.example.androidassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidassignment.databinding.FragmentAdminInfoCenterBinding
import com.example.androidassignment.databinding.FragmentPostLayoutBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [postLayout.newInstance] factory method to
 * create an instance of this fragment.
 */
class postLayout : Fragment() {
    // TODO: Rename and change types of parameters


    private var _binding: FragmentPostLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }




}