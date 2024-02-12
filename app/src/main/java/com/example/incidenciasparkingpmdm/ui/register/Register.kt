package com.example.incidenciasparkingpmdm.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentRegisterBinding

class Register : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toLoginButton.setOnClickListener {
            val action = RegisterDirections.actionRegisterToLogin()
            findNavController().navigate(action)
        }
        binding.registerButton.setOnClickListener {
            findNavController().popBackStack(R.id.welcomeFragment, false)
        }
    }
}