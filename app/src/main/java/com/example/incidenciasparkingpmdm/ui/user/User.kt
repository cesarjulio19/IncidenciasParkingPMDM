package com.example.incidenciasparkingpmdm.ui.user

data class User(val name: String,
    val surname: String,
    val nif: String,
    val email: String,
    val password: String,
    val postalCode: Int,
    val address: String,
    val rol: String,
    val schoolYear: String,
    val parkingAccess: Boolean)

