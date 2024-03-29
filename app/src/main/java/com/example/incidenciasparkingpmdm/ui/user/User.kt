package com.example.incidenciasparkingpmdm.ui.user

import java.io.Serializable

data class User(
    val id: Int?,
    val name: String,
    val surname: String,
    val nif: String,
    val email: String,
    val password: String,
    val postalCode: Int,
    val address: String,
    val rol: String,
    val schoolYear: String,
    val parkingAccess: Boolean): Serializable

data class Credentials(val email: String, val password: String)

