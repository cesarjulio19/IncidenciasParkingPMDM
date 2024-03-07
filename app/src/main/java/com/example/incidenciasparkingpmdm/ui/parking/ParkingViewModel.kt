package com.example.incidenciasparkingpmdm.ui.parking

import androidx.lifecycle.ViewModel
import com.example.incidenciasparkingpmdm.api.IncidentService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class ParkingViewModel @Inject constructor(private val service: IncidentService): ViewModel() {
    fun postRequest(dto: ParkingRequestDto): Call<String> {
        return service.api.addRequest(dto)
    }

    fun postVehicle(dto: VehicleDto): Call<String> {
        return service.api.addVehicle(dto)
    }

    fun deleteRequest(id: Int): Call<String> {
        return service.api.deleteParkingRequest(id)
    }

    fun deleteVehicle(id: Int): Call<String> {
        return service.api.deleteVehicle(id)
    }

    suspend fun getAllParkRequests(): List<ParkingRequest> {
        return service.api.getAllParkingRequests()
    }

    suspend fun getAllVehicles(): List<Vehicle> {
        return service.api.getAllVehicles()
    }
}