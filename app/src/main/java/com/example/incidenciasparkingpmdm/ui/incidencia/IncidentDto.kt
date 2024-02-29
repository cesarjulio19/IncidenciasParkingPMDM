package com.example.incidenciasparkingpmdm.ui.incidencia

import java.io.File
import java.io.Serializable

data class IncidentDto(
                       val title: String,
                       val description: String,
                       val state: Boolean,
                       val userId: Int
): Serializable
