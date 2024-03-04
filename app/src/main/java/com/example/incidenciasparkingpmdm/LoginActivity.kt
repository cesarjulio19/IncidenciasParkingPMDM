package com.example.incidenciasparkingpmdm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.incidenciasparkingpmdm.api.IncidentService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var service: IncidentService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}