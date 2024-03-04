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
import com.example.incidenciasparkingpmdm.ui.user.Credentials
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                try {
                    val credentials = Credentials(binding.emailInput.editText?.text.toString(), binding.passwordInput.editText?.text.toString())
                    Log.e("ERROR", "Contraseña incorrecta")
                    // Realizar el inicio de sesión en segundo plano
                    withContext(Dispatchers.IO) {
                        Log.e("ERROR1", "Contraseña incorrecta")
                        val isLogged = service.apiSinToken.login(credentials)
                        Log.e("ERROR2", "Contraseña incorrecta")
                        if (isLogged) {
                            Log.e("ERROR3", "Contraseña incorrecta")
                            val header = service.getHeader(credentials.email, credentials.password)
                            Log.e("ERROR4", "Contraseña incorrecta")
                            val callUser = service.api.getUserByEmail(header, credentials.email)

                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.putExtra("user", callUser)
                            startActivity(intent)
                        } else {
                            Log.e("ERROR", "Contraseña incorrecta")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Excepción", e.message.toString())
                }
            }
        }
    }
}