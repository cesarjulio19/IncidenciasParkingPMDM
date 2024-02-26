package com.example.incidenciasparkingpmdm.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.MainActivity
import com.example.incidenciasparkingpmdm.api.IncidentService
import com.example.incidenciasparkingpmdm.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    @Inject
    lateinit var service: IncidentService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toRegisterButton.setOnClickListener {
            val action = LoginDirections.actionLoginToRegister()
            findNavController().navigate(action)
        }
        binding.loginButton.setOnClickListener {
            val intent = Intent(this.activity, MainActivity::class.java)
            lifecycleScope.launch {
                val callUser = service.api.getUserByEmail(email = binding.emailInput.editText!!.text.toString());
                if(callUser.password == binding.passwordInput.editText!!.text.toString()) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("user",callUser)
                    startActivity(intent)
                } else {
                    Log.e("ERROR", "Contraseña incorrecta")
                }
            }
        }
    }
}