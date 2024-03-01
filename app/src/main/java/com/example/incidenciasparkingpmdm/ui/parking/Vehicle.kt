package com.example.incidenciasparkingpmdm.ui.parking

import com.example.incidenciasparkingpmdm.ui.user.User

data class Vehicle(val idV: Int,
                   val model:String,
                   val color:String,
                   val licensePlate:String,
                   val user:User)
