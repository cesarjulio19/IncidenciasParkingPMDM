package com.example.incidenciasparkingpmdm.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.MainActivity
import com.example.incidenciasparkingpmdm.databinding.FragmentLoginBinding
import com.example.incidenciasparkingpmdm.ui.user.Credentials
import com.example.incidenciasparkingpmdm.ui.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: UserViewModel by viewModels()

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
                val credentials = Credentials(binding.emailInput.editText?.text.toString(), binding.passwordInput.editText?.text.toString())
                val authHeader = okhttp3.Credentials.basic(credentials.email, credentials.password)
                val isLogged = viewModel.login(credentials)
                viewModel.loginHeader(authHeader)
                if (isLogged) {
                    val callUser = viewModel.getUser(credentials.email);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("user",callUser)
                    startActivity(intent)
                }
                 else {
                    Log.e("ERROR", "Contraseña incorrecta")
                }
            }
        }
    }
}