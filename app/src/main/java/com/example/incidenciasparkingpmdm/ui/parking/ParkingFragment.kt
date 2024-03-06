package com.example.incidenciasparkingpmdm.ui.parking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentParkingBinding
import com.example.incidenciasparkingpmdm.ui.user.User
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ParkingFragment : Fragment() {
    private lateinit var binding: FragmentParkingBinding
    private val viewModel: ParkingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParkingBinding.inflate(inflater, container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        lifecycleScope.launch {
            val user = this@ParkingFragment.requireActivity().intent.getSerializableExtra("user") as? User
            if(hasRequest(user!!)) findNavController().navigate(R.id.parkingSolPenFragment)
            topAppBar.title = getString(R.string.parking_title)
            binding.filledButtonSolicitarParking.setOnClickListener {
                val action = ParkingFragmentDirections.actionParkingFragmentToSolParkingFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun hasRequest(user: User): Boolean {
        var hasRequest = false
        var hasVehicle = false
        val vehicles: MutableList<Vehicle> = mutableListOf()
        val parkingRequests: MutableList<ParkingRequest> = mutableListOf()
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
        parkingRequests.map {
            if(user.id == it.user.id) {
                hasRequest = true
            }
        }
        vehicles.map {
            if(user.id == it.user.id) {
                hasVehicle = true
            }
        }
        return hasRequest && hasVehicle
    }
}