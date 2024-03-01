package com.example.incidenciasparkingpmdm.ui.parking

import com.example.incidenciasparkingpmdm.ui.user.User

data class ParkingRequest(val idReq: Int,
                          val state: Boolean?,
                          val date: String?,
                          val user: User)
