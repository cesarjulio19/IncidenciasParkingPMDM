package com.example.incidenciasparkingpmdm.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.api.IncidentService
import com.example.incidenciasparkingpmdm.databinding.FragmentRegisterBinding
import com.example.incidenciasparkingpmdm.ui.user.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class Register : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    // Servicio para poder utilizar el retrofit
    @Inject
    lateinit var incidentService: IncidentService
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
        /*binding.registerButton.setOnClickListener {
            findNavController().popBackStack(R.id.welcomeFragment, false)
        }*/
        // Lista para el curso escolar
        val list = listOf<String>("DAW","DAM","SMR")
        val mutableList: MutableList<String> = mutableListOf()
        list.forEach{
            mutableList.add(it)
        }
        // Le paso la lista al autocomplete
        val autoCompleteTextView: AutoCompleteTextView = binding.autoComplete
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            mutableList)
        autoCompleteTextView.setAdapter(adapter)
        // Al pulsar el boton de registrar
        binding.registerButton.setOnClickListener {
            // si las 2 contraseñas son iguales
            if(binding.titleInputPassword.text.toString() == binding.titleInputPasswordconf.text.toString()) {

                val codigoPostalString = binding.titleInputCpostal.text.toString()
                // creo las variables de filebyte, file y el codigo postal de tipo entero
                val fileByte: ByteArray = byteArrayOf()
                val file: File = File("")
                val codigoPostalInt: Int = codigoPostalString.toInt()
                /* creo el usuario( no se si esta bien construido ya que no se si tambien
                hay que añadirle el file o ya directamente se lo pasas con el multipart)
                */
                val user: User = User(null,
                  binding.titleInputNombre.text.toString(),
                  binding.titleInputApellido.text.toString(),
                  binding.titleInputDni.text.toString(),
                  binding.titleInputEmail.text.toString(),
                  binding.titleInputPassword.text.toString(),
                  codigoPostalInt,
                  binding.titleInputDirec.text.toString(),
                  "ADMIN",
                  binding.autoComplete.text.toString(),
                  false,
                  //fileByte,
                  //""
                )

                // creo el requestBody del file, al ser la creacion de usuario no tenemos foto
                val fileRequestBody = RequestBody.create(
                  MediaType
                      .parse("multipart/form-data"), file
                )
                // creo el filePart
                val filePart = MultipartBody.Part
                  .createFormData("file", file.name, fileRequestBody)

                lifecycleScope.launch {
                    try {
                        val csrfToken = incidentService.apiSinToken.getCsrfToken().execute().body()?.token
                        incidentService.updateCsrfToken(csrfToken ?: "")

                        // Realizar el registro en segundo plano
                        withContext(Dispatchers.IO) {
                            val call = incidentService.api.addNewUser(user)
                            call.enqueue(object : Callback<String> {
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    if(!response.isSuccessful) {
                                        Log.e("No esito","no tuvo esito")
                                    } else {
                                        Log.e("Esito", "tuvo esito")
                                    }
                                    response.body()
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.e("Fallo","Fallo")
                                }
                            })
                        }

                        val action = RegisterDirections.actionRegisterToLogin()
                        findNavController().navigate(action)
                    } catch (e: Exception) {
                        Log.e("Excepción", e.message.toString())
                    }
                    /*val csrfToken = incidentService.apiSinToken.getCsrfToken().execute().body()?.token
                    incidentService.updateCsrfToken(csrfToken ?: "")
                    var call = incidentService.api.addNewUser(user)
                    call.enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if(!response.isSuccessful) {
                              Log.e("No esito","no tuvo esito")
                            } else {
                                Log.e("Esito", "tuvo esito")
                            }
                            response.body()
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.e("Fallo","Fallo")
                        }
                    })
                    val action = RegisterDirections.actionRegisterToLogin()
                    findNavController().navigate(action)*/
                    val action = RegisterDirections.actionRegisterToLogin()
                    findNavController().navigate(action)
                }
            } else {
              Log.e("ERROR", "Contraseñas no iguales")
            }
        }
    }
}