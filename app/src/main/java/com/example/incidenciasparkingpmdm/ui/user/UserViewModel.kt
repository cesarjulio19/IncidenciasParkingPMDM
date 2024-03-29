package com.example.incidenciasparkingpmdm.ui.user

import androidx.lifecycle.ViewModel
import com.example.incidenciasparkingpmdm.api.IncidentService
import com.example.incidenciasparkingpmdm.ui.login.Holder
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val service: IncidentService, private val holder: Holder): ViewModel() {
    suspend fun login(credentials: Credentials): Boolean {
        return service.api.login(credentials)
    }

    fun loginHeader(header: String) {
        return holder.setCredentials(header)
    }

    fun register(user: User): Call<String> {
        return service.api.addNewUser(user)
    }

    suspend fun getUser(email:String): User {
        return service.api.getUserByEmail(email)
    }
    suspend fun updateUser(id: Int, user: User): Call<String> {
        return service.api.updateUser(id, user)
    }
}