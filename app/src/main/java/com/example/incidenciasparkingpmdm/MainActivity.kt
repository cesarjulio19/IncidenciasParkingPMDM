package com.example.incidenciasparkingpmdm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.incidenciasparkingpmdm.api.IncidentService
import com.example.incidenciasparkingpmdm.databinding.ActivityMainBinding
import com.example.incidenciasparkingpmdm.ui.parking.ParkingRequest
import com.example.incidenciasparkingpmdm.ui.user.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var service: IncidentService
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    var pRequest: ParkingRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = this.intent.getSerializableExtra("user") as? User
        val navView: BottomNavigationView = binding.bottomNavigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        lifecycleScope.launch {
            val parkingRequests = service.api.getAllParkingRequests()
            parkingRequests.map {
                if(user != null && it.user.id == user.id) {
                    pRequest = it
                }
            }
            navController = navHostFragment.navController
            if(pRequest != null) {
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.incidenciaFragment,
                        R.id.parkingSolPenFragment
                    )
                )
            } else {
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.incidenciaFragment,
                        R.id.parkingFragment
                    )
                )
            }
            navView.setupWithNavController(navController)
        }
    }
}