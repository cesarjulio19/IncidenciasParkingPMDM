package com.example.incidenciasparkingpmdm.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.incidenciasparkingpmdm.ui.incidencia.Incident
import com.example.incidenciasparkingpmdm.ui.incidencia.IncidentDto
import com.example.incidenciasparkingpmdm.ui.parking.ParkingRequest
import com.example.incidenciasparkingpmdm.ui.parking.ParkingRequestDto
import com.example.incidenciasparkingpmdm.ui.parking.Vehicle
import com.example.incidenciasparkingpmdm.ui.parking.VehicleDto
import com.example.incidenciasparkingpmdm.ui.user.User
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
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
import javax.inject.Named
import javax.inject.Singleton
// Interfaz para los endPoint
interface IncidentApi{
    @POST("register")
    fun addNewUser(
        @Body user: User,
        //@Part file: MultipartBody.Part?
    ): Call<String>
    @GET("csrf")
    fun getCsrfToken(): Call<CsrfToken>

    @GET("api/users/{email}")
    suspend fun getUserByEmail(@Header("Authorization") authHeader: String, @Path("email") email: String): User

    @POST("login")
    suspend fun login(/*@Header("Authorization") authHeader: String*/@Body credentials: com.example.incidenciasparkingpmdm.ui.user.Credentials): Boolean

    @Multipart
    @POST("api/incidents")
    suspend fun addIncident(@Header("Authorization") authHeader: String, @Part("incident") incident: IncidentDto,
                            @Part file: MultipartBody.Part): Call<String>

    @Multipart
    @PUT("api/incidents/{idInc}")
    suspend fun updateIncident(@Header("Authorization") authHeader: String,@Path("idInc") idInc: Int,
                               @Part("incident") incident: IncidentDto,
                               @Part file: MultipartBody.Part): Call<String>

    @GET("api/incidents")
    suspend fun getAllIncidents(@Header("Authorization") authHeader: String): List<Incident>

    @GET("api/incidents/{idInc}")
    suspend fun getIncident(@Header("Authorization") authHeader: String,@Path("idInc") idInc: Int): Incident

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

class CsrfInterceptor(private val csrfToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        Log.d("TOKEN", csrfToken)
        val requestWithCsrf = originalRequest.newBuilder()
            .header("X-CSRF-TOKEN", csrfToken)
            .build()
        return chain.proceed(requestWithCsrf)
    }
}

/* Clase Singleton para el IncidentService, donde obtienes el api ya construido con retrofit
   y tambien esta comentado otro apiCsrf para cuando se necesite realizar acciones con el token en
   la cabecera(No se si esta bien echo, el código con el csrf esta comentado)
 */
@Singleton
class IncidentService @Inject constructor() {
    private val _incidentList = MutableLiveData<List<Incident>>()
    val incidentList: LiveData<List<Incident>>
        get() {
            return _incidentList
        }

    suspend fun fetch(email: String, password: String) {
        val header = getHeader(email, password)
        _incidentList.value = apiSinToken.getAllIncidents(header).map {
            Incident(it.idInc, it.title, it.description, it.state, it.date, it.userId, it.file, it.fileType)
        }
    }

    suspend fun getIncident(email:String, password: String, id: Int): Incident {
        val header = getHeader(email, password)
        return apiSinToken.getIncident(header, id)
    }

    // Cambialo a como tengas la ip de tu pc, luego ya probaremos con la dirección de la api remoto
    private val direccionHttp:String = "http://192.168.1.42:8080/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(direccionHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiSinToken:IncidentApi = retrofit.create(IncidentApi::class.java)

    lateinit var api: IncidentApi
        private set

    init {
        buildRetrofitApi()

    }

    /*suspend fun login(credentials: Credentials):Boolean {
        val concatCredentials = "${credentials.email}:${credentials.password}"
        val authHeader = "Basic "+ Base64.encodeToString(concatCredentials.toByteArray(), Base64.NO_WRAP)
        return try {
            api.login(authHeader)
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            return false;
        }
    }*/

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(direccionHttp)
        .addConverterFactory(GsonConverterFactory.create())
    fun getHeader(email: String, password:String):String {
        return Credentials.basic(email, password)
    }
    private fun buildRetrofitApi() {
        val retrofit = retrofitBuilder?.build()
        if (retrofit != null) {
            api = retrofit.create(IncidentApi::class.java)
        } else {

            Log.e("Error", "retrofitBuilder es nulo al construir Retrofit")
        }
    }

    fun updateCsrfToken(token: String) {
        val interceptor = CsrfInterceptor(token)
        val retrofitCsrf = retrofitBuilder.client(
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        ).build()

        api = retrofitCsrf.create(IncidentApi::class.java)
    }

}