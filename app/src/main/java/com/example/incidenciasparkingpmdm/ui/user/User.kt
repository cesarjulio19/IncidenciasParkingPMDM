package com.example.incidenciasparkingpmdm.ui.user

import java.io.File

data class User(val name: String,
    val surname: String,
    val nif: String,
    val email: String,
    val password: String,
    val postalcode: Int,
    val address: String,
    val rol: String,
    val schoolyear: String,
    val parkingAccess: Boolean)
    //val file: ByteArray,
    //val typeFile: String)
