package com.example.incidenciasparkingpmdm.ui.parking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.incidenciasparkingpmdm.api.IncidentService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class ParkingViewModel @Inject constructor(private val service: IncidentService): ViewModel() {
    private val _parkingRequests = MutableLiveData<List<ParkingRequest>>()
    val parkingRequest:LiveData<List<ParkingRequest>>
        get() = _parkingRequests

    private val _vehicleList = MutableLiveData<List<Vehicle>>()

    val vehicleList:LiveData<List<Vehicle>>
        get() = _vehicleList

    private val observerRequests = Observer<List<ParkingRequest>> {
        val list = it.map {
            ParkingRequest(it.idReq, it.state, it.date, it.user)
        }
        _parkingRequests.value = list
    }
    private val observerVehicles = Observer<List<Vehicle>> {
        val list = it.map {
            Vehicle(it.idV, it.model, it.color, it.licensePlate, it.user)
        }
        _vehicleList.value = list
    }
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

    init {
        fetch()
    }
    private fun fetch() {
        service.parkingRequests.observeForever(observerRequests)
        service.vehicleList.observeForever(observerVehicles)
        viewModelScope.launch {
            service.fetchParkRequests()
            service.fetchVehicles()
        }
    }
}