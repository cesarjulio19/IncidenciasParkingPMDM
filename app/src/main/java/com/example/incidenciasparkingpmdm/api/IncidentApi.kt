package com.example.incidenciasparkingpmdm.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.incidenciasparkingpmdm.ui.incidencia.Incident
import com.example.incidenciasparkingpmdm.ui.incidencia.IncidentDto
import com.example.incidenciasparkingpmdm.ui.parking.ParkingRequest
import com.example.incidenciasparkingpmdm.ui.parking.ParkingRequestDto
import com.example.incidenciasparkingpmdm.ui.parking.Vehicle
import com.example.incidenciasparkingpmdm.ui.parking.VehicleDto
import com.example.incidenciasparkingpmdm.ui.user.Credentials
import com.example.incidenciasparkingpmdm.ui.user.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton
// Interfaz para los endPoint
interface IncidentApi{
    @POST("register")
    fun addNewUser(
        @Body user: User,
        //@Part file: MultipartBody.Part?
    ): Call<String>
    @GET("csrf")
    suspend fun getCsrfToken(@Header("Authorization") authHeader: String): CsrfToken

    @GET("api/users/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User

    @POST("login")
    suspend fun login(@Body credentials: Credentials): Boolean

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

/*class CsrfInterceptor(private val csrfToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val requestWithCsrf = originalRequest.newBuilder()
            .header("X-CSRF-TOKEN", csrfToken)
            .build()
        return chain.proceed(requestWithCsrf)
    }
}*/
/* Clase Singleton para el IncidentService, donde obtienes el api ya construido con retrofit
   y tambien esta comentado otro apiCsrf para cuando se necesite realizar acciones con el token en
   la cabecera(No se si esta bien echo, el código con el csrf esta comentado)
 */
@Singleton
class IncidentService @Inject constructor(){
    private val _incidentList = MutableLiveData<List<Incident>>()
    val incidentList: LiveData<List<Incident>>
        get() {
            return _incidentList
        }

    suspend fun fetch() {
        _incidentList.value = api.getAllIncidents().map {
            Incident(it.idInc, it.title, it.description, it.state, it.date, it.userId, it.file, it.fileType)
        }
    }

    suspend fun getIncident(id: Int): Incident {
        return api.getIncident(id)
    }

    // Cambialo a como tengas la ip de tu pc, luego ya probaremos con la dirección de la api remoto
    private val direccionHttp:String = "http://192.168.1.59:8080/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(direccionHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api:IncidentApi = retrofit.create(IncidentApi::class.java)
    /*var csrfToken:CsrfToken = CsrfToken("")
    val interceptor = CsrfInterceptor(csrfToken.x_csrf_token)

    private val retrofitCsrf = Retrofit.Builder()
        .baseUrl("http://localhost:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        )
        .build()

    val apicsrf:IncidentApi = retrofitCsrf.create(IncidentApi::class.java)

     suspend fun getCsrf(username: String, password: String): CsrfToken?{
        val credentials = "$username:$password"
        val authHeader = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        try {
            csrfToken = api.getCsrfToken(authHeader)
            return csrfToken
        } catch (e: Exception) {
            Log.e("Error", "${e.toString()}")
        }
        return csrfToken
     }*/
}