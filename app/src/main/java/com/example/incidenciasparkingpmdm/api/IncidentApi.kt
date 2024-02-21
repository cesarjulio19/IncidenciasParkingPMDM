package com.example.incidenciasparkingpmdm.api

import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import javax.inject.Inject
import javax.inject.Singleton
// Interfaz para los endPoint
interface IncidentApi{
    @Multipart
    @POST("/api/users")
    suspend fun addNewUser(
        @Part("user") user: RequestBody,
        @Part filePart: MultipartBody.Part
    ): retrofit2.Response<String>

    @GET("/getCsrfToken")
    suspend fun getCsrfToken(@Header("Authorization") authHeader: String): CsrfToken

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
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080")
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