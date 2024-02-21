package com.example.incidenciasparkingpmdm.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.Application
import com.example.incidenciasparkingpmdm.MainActivity
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.api.IncidentService
import com.example.incidenciasparkingpmdm.databinding.FragmentRegisterBinding
import com.example.incidenciasparkingpmdm.ui.user.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject
import com.google.gson.Gson
import okhttp3.MultipartBody
import java.io.File

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
                  if(binding.titleInputPassword.toString() == binding.titleInputPasswordconf.toString()) {

                      val codigoPostalString = binding.titleInputCpostal.text.toString()
                      // creo las variables de filebyte, file y el codigo postal de tipo entero
                      val fileByte: ByteArray = byteArrayOf()
                      val file: File = File("")
                      val codigoPostalInt: Int = codigoPostalString.toInt()
                      /* creo el usuario( no se si esta bien construido ya que no se si tambien
                      hay que añadirle el file o ya directamente se lo pasas con el multipart)
                       */
                      val user: User = User(
                          binding.titleInputNombre.text.toString(),
                          binding.titleInputApellido.text.toString(),
                          binding.titleInputDni.text.toString(),
                          binding.titleInputEmail.text.toString(),
                          binding.titleInputPassword.text.toString(),
                          codigoPostalInt,
                          binding.titleInputDirec.text.toString(),
                          "Admin",
                          binding.autoComplete.text.toString(),
                          false,
                          //fileByte,
                          //""
                      )
                      // creo el requestBody del usuario convirtiendolo en json
                      val userRequestBody = RequestBody.create(
                          MediaType.parse("application/json"),
                          Gson().toJson(user)
                      )
                      // creo el requestBody del file, al ser la creacion de usuario no tenemos foto
                      val fileRequestBody = RequestBody.create(
                          MediaType
                              .parse("multipart/form-data"), file
                      )
                      // creo el filePart
                      val filePart = MultipartBody.Part
                          .createFormData("file", file.name, fileRequestBody)

                      viewLifecycleOwner.lifecycleScope.launch {
                          try {
                              /* llamo al incidentService para realizar el addNewUser y le paso
                              el  userRequestBody y el filePart
                               */
                              val response = incidentService.api.
                              addNewUser(userRequestBody, filePart)
                              // si se ha realizado con exito navego al login, si no no hace nada
                              if (response.isSuccessful) {

                                  val responseBody = response.body()
                                  Log.e("DENTRO", "OPERACION REALIZADA CON ÉXITO")
                                  val action = RegisterDirections.actionRegisterToLogin()
                                  findNavController().navigate(action)
                              } else {
                                  Log.e("NO DENTRO", "OPERACIÓN FALLIDA")
                                  val errorBody = response.errorBody()
                              }
                          } catch (e: Exception) {

                              e.printStackTrace()
                          }
                      }

                  }
        }



    }
}