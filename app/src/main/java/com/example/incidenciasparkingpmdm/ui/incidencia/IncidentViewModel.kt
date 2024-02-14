package com.example.incidenciasparkingpmdm.ui.incidencia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class IncidentViewModel: ViewModel() {

    private val _fileCapture = MutableLiveData<File>()
    val fileCapture: LiveData<File> get() = _fileCapture

    fun updateFileData(data: File){
        _fileCapture.value = data
    }

}