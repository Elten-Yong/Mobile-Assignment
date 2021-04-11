package com.example.androidassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidassignment.databinding.InfoCenterFragmentBinding

class infoCenter : Fragment() {

    private var _binding: InfoCenterFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = infoCenter()
    }


    private lateinit var viewModel: InfoCenterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.info_center_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InfoCenterViewModel::class.java)
        // TODO: Use the ViewModel
    }

}