package com.example.incidenciasparkingpmdm.ui.incidencia

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class IncidentViewModel: ViewModel() {

    private val _fileCapture = MutableLiveData<File>()
    val fileCapture: LiveData<File> get() = _fileCapture

    private val _uri = MutableLiveData<Uri>()

    val uri: MutableLiveData<Uri> get() = _uri

    fun updateFileData(data: File){
        _fileCapture.value = data
    }

    fun updateUriData(data: Uri){
        _uri.value = data
    }

}