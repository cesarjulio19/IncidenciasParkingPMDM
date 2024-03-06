package com.example.incidenciasparkingpmdm.ui.parking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentParkingSolPenBinding
import com.example.incidenciasparkingpmdm.ui.user.User
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class ParkingSolPenFragment : Fragment() {
    private lateinit var binding: FragmentParkingSolPenBinding
    private val viewModel: ParkingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParkingSolPenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        lifecycleScope.launch {
            topAppBar.title = getString(R.string.parking_title)
            topAppBar.navigationIcon = null
            val vehicles: MutableList<Vehicle> = mutableListOf()
            val parkingRequests: MutableList<ParkingRequest> = mutableListOf()
            lateinit var vehicle:Vehicle
            lateinit var pRequest:ParkingRequest
            val observerRequests = Observer<List<ParkingRequest>> {
                it.map {
                        request -> parkingRequests.add(request)
                }
            }
            val observerVehicles = Observer<List<Vehicle>> {
                it.map {
                        vehicle -> vehicles.add(vehicle)
                }
            }
            viewModel.parkingRequest.observe(viewLifecycleOwner, observerRequests)
            viewModel.vehicleList.observe(viewLifecycleOwner, observerVehicles)
            val user = this@ParkingSolPenFragment.requireActivity().intent.getSerializableExtra("user") as? User
            Log.e("UserID del user", user?.id.toString())
            vehicles.map {
                if(it.user.id == user?.id) {
                    Log.e("UserID del vehiculo correcto", it.user.id.toString())
                    vehicle = it
                }
            }
            parkingRequests.map {
                if(it.user.id == user?.id) {
                    Log.e("UserID de la solicitud correcto", it.user.id.toString())
                    pRequest = it
                }
            }
            if(pRequest.state == false) {
                Log.e("Vehiculo",vehicle.model)
                Log.e("Solicitud",pRequest.date.toString())
                binding.colorData.text = vehicle.color
                binding.marcaModeloData.text = vehicle.model
                binding.matriculaData.text = vehicle.licensePlate
                binding.stateData.text = getString(R.string.pending_parking_request)
                binding.date.text = pRequest.date
                binding.buttonDelete.setOnClickListener {
                    val deleteReq = viewModel.deleteRequest(pRequest.idReq)
                    val deleteVehicle = viewModel.deleteVehicle(vehicle.idV)
                    deleteReq.enqueue(object : Callback<String> {
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
                    deleteVehicle.enqueue(object : Callback<String> {
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
                    findNavController().popBackStack(R.id.parkingFragment, true)
                }
            }

        }
    }
}