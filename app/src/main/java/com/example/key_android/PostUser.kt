package com.example.key_android
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

data class UserData(
    val username: String,
    val password: String
)

interface PostUser {
    @POST("andro")
    fun userLogin(@Body credentials: UserData): Call<User>
}

object ConectServ {
    private const val BASE_URL = "http://10.0.2.2:5000"

    val instance: PostUser by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PostUser::class.java)
    }
}