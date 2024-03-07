package com.example.incidenciasparkingpmdm.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.incidenciasparkingpmdm.AppModule.baseURL
import com.example.incidenciasparkingpmdm.ui.incidencia.Incident
import com.example.incidenciasparkingpmdm.ui.incidencia.IncidentDto
import com.example.incidenciasparkingpmdm.ui.login.Holder
import com.example.incidenciasparkingpmdm.ui.parking.ParkingRequest
import com.example.incidenciasparkingpmdm.ui.parking.ParkingRequestDto
import com.example.incidenciasparkingpmdm.ui.parking.Vehicle
import com.example.incidenciasparkingpmdm.ui.parking.VehicleDto
import com.example.incidenciasparkingpmdm.ui.user.User
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

// Interfaz para los endPoint
interface IncidentApi{
    // USER
    @POST("register")
    fun addNewUser(
        @Body user: User,
        //@Part file: MultipartBody.Part?
    ): Call<String>

    @GET("csrf")
    fun getCsrf(): Call<CsrfToken>

    @GET("api/users/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User

    @POST("login")
    suspend fun login(@Body credentials: com.example.incidenciasparkingpmdm.ui.user.Credentials): Boolean

    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): Call<String>

    // INCIDENTS
    @Multipart
    @POST("api/incidents")
    suspend fun addIncident(@Part("incident") incident: IncidentDto,
                            @Part file: MultipartBody.Part): Call<String>

    @Multipart
    @PUT("api/incidents/{idInc}")
    suspend fun updateIncident(@Path("idInc") idInc: Int,
                               @Part("incident") incident: IncidentDto,
                               @Part file: MultipartBody.Part): Call<String>

    @GET("api/incidents")
    suspend fun getAllIncidents(): List<Incident>

    @GET("api/incidents/{idInc}")
    suspend fun getIncident(@Path("idInc") idInc: Int): Incident

    @DELETE("api/incidents/{idInc}")
    fun deleteIncident(@Path("idInc") idInc: Int): Call<String>

    // PARKING
    @POST("api/vehicles")
    fun addVehicle(@Body vehicle: VehicleDto): Call<String>

    @POST("api/parking_requests")
    fun addRequest(@Body pRequest: ParkingRequestDto): Call<String>

    @GET("api/parking_requests")
    suspend fun getAllParkingRequests(): List<ParkingRequest>

    @DELETE("api/parking_requests/{idReq}")
    fun deleteParkingRequest(@Path("idReq") idReq:Int): Call<String>

    @GET("api/vehicles")
    suspend fun getAllVehicles(): List<Vehicle>

    @DELETE("api/vehicles/{idV}")
    fun deleteVehicle(@Path("idV") idV: Int): Call<String>
}

class AuthInterceptor @Inject constructor (private val authHeader:Holder) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()

        val requestWithAuth = originalRequest.newBuilder()
            .addHeader("Authorization", authHeader.credentials)
            .build()
        return chain.proceed(requestWithAuth)

    }
}

/* Clase Singleton para el IncidentService, donde obtienes el api ya construido con retrofit
   y tambien esta comentado otro apiCsrf para cuando se necesite realizar acciones con el token en
   la cabecera(No se si esta bien echo, el código con el csrf esta comentado)
 */
@Singleton
class IncidentService @Inject constructor(private val interceptor: AuthInterceptor) {
    private val retrofit = Retrofit.Builder().baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build())
        .build()
    val api: IncidentApi = retrofit.create(IncidentApi::class.java)

    private val _incidentList = MutableLiveData<List<Incident>>()
    val incidentList: LiveData<List<Incident>>
        get() {
            return _incidentList
        }

    private val _parkingRequests = MutableLiveData<List<ParkingRequest>>()
    val parkingRequests :LiveData<List<ParkingRequest>>
        get() = _parkingRequests

    private val _vehicleList = MutableLiveData<List<Vehicle>>()
    val vehicleList: LiveData<List<Vehicle>>
        get() = _vehicleList

    suspend fun fetchIncidents() {
        _incidentList.value = api.getAllIncidents().map {
            Incident(
                it.idInc,
                it.title,
                it.description,
                it.state,
                it.date,
                it.user,
                it.file,
                it.fileType
            )
        }
    }

    suspend fun fetchParkRequests() {
        _parkingRequests.value = api.getAllParkingRequests().map {
            ParkingRequest(it.idReq, it.state, it.date, it.user)
        }
    }

    suspend fun fetchVehicles() {
        _vehicleList.value = api.getAllVehicles().map {
            Vehicle(it.idV, it.model, it.color, it.licensePlate, it.user)
        }
    }

    suspend fun getIncident(id: Int): Incident {
        return api.getIncident(id)
    }

}