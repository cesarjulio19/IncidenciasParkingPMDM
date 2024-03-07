package com.example.incidenciasparkingpmdm.ui.incidencia

import com.example.incidenciasparkingpmdm.ui.user.User

data class Incident(
    val idInc: Int?,
    val title: String,
    val description: String,
    val state: Boolean?,
    val date: String?,
    val user: User,
    val file: String,
    val fileType: String
)
