package com.example.incidenciasparkingpmdm.api

import com.google.gson.annotations.SerializedName

/* data class de CsrfToken, no creo que este bien ya que no me acuerdo exactamente cual era la
estructura que devolvia
 */
data class CsrfToken(
    @SerializedName("token")
    val token: String
)
