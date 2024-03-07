package com.example.incidenciasparkingpmdm.ui.login

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Holder @Inject constructor() {
    private var _credentials: String = ""
    val credentials
        get() = _credentials

    fun setCredentials(credentialLogin:String) {
        _credentials = credentialLogin
    }
}