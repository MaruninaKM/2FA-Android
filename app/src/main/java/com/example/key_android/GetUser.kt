package com.example.key_android
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface GetUser {
    @GET("key")
    fun fetchData(): Call<Kod>
    @POST("key")
    fun sendLogin(@Body login: LoginData): Call<Void>
}

data class LoginData(val login: String)


object GetKey {
    private const val BASE_URL = "http://10.0.2.2:5000"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apice: GetUser = retrofit.create(GetUser::class.java)
}