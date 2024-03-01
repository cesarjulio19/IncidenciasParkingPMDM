package com.example.incidenciasparkingpmdm.ui.parking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.api.IncidentService
import com.example.incidenciasparkingpmdm.databinding.FragmentParkingBinding
import com.example.incidenciasparkingpmdm.ui.user.User
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ParkingFragment : Fragment() {
    private lateinit var binding: FragmentParkingBinding
    @Inject
    lateinit var service: IncidentService
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
        val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawerLayout)
        val navigationView: NavigationView = requireActivity().findViewById(R.id.navigation_view)
        lifecycleScope.launch {
            val user = this@ParkingFragment.requireActivity().intent.getSerializableExtra("user") as? User
            if(hasRequest(user!!)) findNavController().navigate(R.id.parkingSolPenFragment)
            topAppBar.title = getString(R.string.parking_title)
            topAppBar.setNavigationIcon(R.drawable.ic_launcher_foreground)
            topAppBar.setNavigationOnClickListener {
                drawerLayout.open()
            }
            navigationView.setNavigationItemSelectedListener {
                it.isChecked = true
                drawerLayout.close()
                true
            }
            binding.filledButtonSolicitarParking.setOnClickListener {
                val action = ParkingFragmentDirections.actionParkingFragmentToSolParkingFragment()
                findNavController().navigate(action)
            }
        }

    }

    private suspend fun hasRequest(user: User): Boolean {
        var hasRequest = false
        var hasVehicle = false
        val parkingRequests = service.api.getAllParkingRequests()
        val vehicleRequests = service.api.getAllVehicles()
        parkingRequests.map {
            if(user.id == it.user.id) {
                hasRequest = true
            }
        }
        vehicleRequests.map {
            if(user.id == it.user.id) {
                hasVehicle = true
            }
        }
        return hasRequest && hasVehicle
    }
}