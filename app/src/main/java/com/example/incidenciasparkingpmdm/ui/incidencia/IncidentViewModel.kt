package com.example.incidenciasparkingpmdm.ui.incidencia

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.incidenciasparkingpmdm.api.IncidentService
import com.example.incidenciasparkingpmdm.ui.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody.Part
import retrofit2.Call
import java.io.File
import javax.inject.Inject

@HiltViewModel
class IncidentViewModel @Inject constructor(private val service: IncidentService): ViewModel() {
    private val _incidentList = MutableLiveData<List<Incident>>()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user
    private val userObserver = Observer<User> {
        _user.value = it
    }
    val incidentList: LiveData<List<Incident>>
        get() {
            return _incidentList
        }
    private val observer = Observer<List<Incident>> {
        val list = it.map {
            Incident(it.idInc, it.title, it.description, it.state, it.date, it.user, it.file, it.fileType)
        }

        _incidentList.value = list
    }
    private val _fileCapture = MutableLiveData<File>()
    val fileCapture: LiveData<File> get() = _fileCapture

    private val _uri = MutableLiveData<Uri>()

    val uri: MutableLiveData<Uri> get() = _uri

    private val _uriEdit = MutableLiveData<Uri>()

    val uriEdit: MutableLiveData<Uri> get() = _uriEdit

    private val _incident = MutableLiveData<Incident>()
    val incident: LiveData<Incident>
        get() {
            return _incident
        }

    fun updateFileData(data: File){
        _fileCapture.value = data
    }

    fun updateUriData(data: Uri){
        _uri.value = data
    }

    fun updateUriEditData(data: Uri){
        _uriEdit.value = data
    }

    init {
        _user.observeForever(userObserver)
       fetch()
    }

    fun fetch() {
        service.incidentList.observeForever(observer)
        viewModelScope.launch {
            service.fetchIncidents()
        }
    }

    fun fetchIncident(id:Int) {
        viewModelScope.launch {
            _incident.value = service.getIncident(id)
        }
    }
    suspend fun createIncident(dto: IncidentDto, filePart: Part): Call<String> {
        return service.api.addIncident(dto, filePart)
    }

    suspend fun updateIncident(id: Int, dto: IncidentDto, filePart: Part): Call<String> {
        return service.api.updateIncident(id, dto, filePart)
    }

    fun deleteIncident(id: Int):Call<String> {
        return service.api.deleteIncident(id)
    }
}