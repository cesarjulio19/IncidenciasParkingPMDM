package com.example.incidenciasparkingpmdm.ui.parking

import java.io.Serializable

data class VehicleDto(
    val model:String,
    val color:String,
    val licensePlate:String,
    val user_id:Int): Serializable
