package com.example.incidenciasparkingpmdm.ui.parking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        topAppBar.navigationIcon = null
        lifecycleScope.launch {
            val user = this@ParkingFragment.requireActivity().intent.getSerializableExtra("user") as? User
            val parkingRequests = viewModel.getAllParkRequests()
            val vehicles = viewModel.getAllVehicles()
            if(hasRequest(user!!, parkingRequests, vehicles)) findNavController().navigate(R.id.parkingSolPenFragment)
            topAppBar.title = getString(R.string.parking_title)
            binding.filledButtonSolicitarParking.setOnClickListener {
                val action = ParkingFragmentDirections.actionParkingFragmentToSolParkingFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun hasRequest(user: User, parkingRequests: List<ParkingRequest>, vehicles:List<Vehicle>): Boolean {
        var hasRequest = false
        var hasVehicle = false
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