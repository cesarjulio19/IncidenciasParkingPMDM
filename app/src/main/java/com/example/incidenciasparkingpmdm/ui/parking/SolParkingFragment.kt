package com.example.incidenciasparkingpmdm.ui.parking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentSolParkingBinding
import com.example.incidenciasparkingpmdm.ui.user.User
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class SolParkingFragment: Fragment() {
    private lateinit var binding: FragmentSolParkingBinding
    private val viewModel : ParkingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSolParkingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = this.requireActivity().intent.getSerializableExtra("user") as? User
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        topAppBar.title = getString(R.string.parking_request)
        topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        if(user != null) {
            binding.filledButtonSolicitarParking.setOnClickListener {
                val vehicle = VehicleDto(binding.titleInputMarc.text.toString(),
                    binding.titleInputColor.text.toString(),
                    binding.titleInputMatr.text.toString(),
                    user.id!!)
                val pRequest = ParkingRequestDto(false, null, user.id)
                val callVehicle = viewModel.postVehicle(vehicle)
                val callRequest = viewModel.postRequest(pRequest)
                callVehicle.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful) {
                            Log.e("Exito", response.body().toString())
                        } else {
                            Log.e("No exito","no exito")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("Fallo", t.message.toString())
                    }

                })
                callRequest.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful) {
                            Log.e("Exito", response.body().toString())
                        } else {
                            Log.e("No exito","no exito")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("Fallo", t.message.toString())
                    }

                })
                val action =
                    SolParkingFragmentDirections
                        .actionSolParkingFragmentToParkingSolPenFragment()
                findNavController().navigate(action)
            }
        }
    }
}