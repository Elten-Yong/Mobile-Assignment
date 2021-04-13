package com.example.androidassignment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidassignment.databinding.ProfileFragmentBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class profile : Fragment() {
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = profile()
    }



    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnSignOut.setOnClickListener{
            Firebase.auth.signOut()

            startActivity(Intent(getActivity(),LoginActivity::class.java))

            getActivity()?.finish()
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}