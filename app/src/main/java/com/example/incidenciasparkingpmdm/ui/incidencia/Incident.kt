package com.example.incidenciasparkingpmdm.ui.incidencia

import java.io.File

data class Incident(val id: Int?,
    val title: String,
    val description: String,
    val state: Boolean,
    val date: String?,
    val userId: Int)
