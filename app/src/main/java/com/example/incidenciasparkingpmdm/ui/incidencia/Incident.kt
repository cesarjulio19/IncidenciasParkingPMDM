package com.example.incidenciasparkingpmdm.ui.incidencia

data class Incident(
    val idInc: Int?,
    val title: String,
    val description: String,
    val state: Boolean?,
    val date: String?,
    val userId: Int,
    val file: String,
    val fileType: String
)
