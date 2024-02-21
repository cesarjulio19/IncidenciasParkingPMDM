package com.example.incidenciasparkingpmdm.ui.welcome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToLogin()
            findNavController().navigate(action)
        }
        binding.registerButton.setOnClickListener {
            try {
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToRegister()
                findNavController().navigate(action)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("NAVEGACION", "${e.printStackTrace()}")
            }
        }
    }
}