package com.example.incidenciasparkingpmdm.ui.incidencia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.api.IncidentService
import com.example.incidenciasparkingpmdm.databinding.FragmentCreateInBinding
import com.example.incidenciasparkingpmdm.ui.user.User
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class CreateInFragment : Fragment() {
    private lateinit var binding: FragmentCreateInBinding
    @Inject
    lateinit var incidentService: IncidentService
    private val incidentViewModel: IncidentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        topAppBar.title = getString(R.string.add_inc_title)
        topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.textButtonArchivo.setOnClickListener {
             val action = CreateInFragmentDirections
                 .actionCreateInFragmentToPreviewCameraFragment()

            view.findNavController().navigate(action)

        }

        incidentViewModel.fileCapture.observe(viewLifecycleOwner) { data ->

            Glide.with(this)
                .load(data)
                .into(binding.image)

        }

        binding.textButtonGaleria.setOnClickListener {
            pickPhotoFromGallery()
        }

        binding.filledButtonCreate.setOnClickListener {
            val user = this.activity?.intent?.getSerializableExtra("user") as? User
            val incident = Incident(null, binding.titleInputTitulo.text.toString(), binding.titleInputDesc.text.toString(), null, null, user?.id!!)
            lifecycleScope.launch {
                val callIncident = incidentService.api.addIncident(incident)
                callIncident.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Log.e("Exsito", response.body().toString())
                        } else {
                            Log.e("No existo", "No exsito");
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        if (t.cause != null) {
                            Log.e("ERROR", t.message.toString())
                        }
                    }
                })
                findNavController().popBackStack()
            }
        }
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->

        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data

            Glide.with(this)
                .load(data)
                .into(binding.image)
        }

    }

    private fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }
}