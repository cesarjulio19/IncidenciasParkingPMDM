package com.example.incidenciasparkingpmdm.ui.incidencia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.incidenciasparkingpmdm.api.IncidentService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
@HiltViewModel
class IncidentViewModel @Inject constructor(private val service: IncidentService): ViewModel() {
    private val _incidentList = MutableLiveData<List<Incident>>()
    val incidentList: LiveData<List<Incident>>
        get() {
            return _incidentList
        }
    private val observer = Observer<List<Incident>> {
        val list = it.map {
            Incident(it.idInc, it.title, it.description, it.state, it.date, it.userId)
        }
        _incidentList.value = list
    }
    private val _fileCapture = MutableLiveData<File>()
    val fileCapture: LiveData<File> get() = _fileCapture

    private val _incident = MutableLiveData<Incident>()
    val incident: LiveData<Incident>
        get() {
            return _incident
        }

    fun updateFileData(data: File){
        _fileCapture.value = data
    }

    init {
        fetch()
    }

    fun fetch() {
        service.incidentList.observeForever(observer)
        viewModelScope.launch {
            service.fetch()
        }
    }

    fun fetchIncident(id:Int) {
        viewModelScope.launch {
            _incident.value = service.getIncident(id)
        }
    }
}