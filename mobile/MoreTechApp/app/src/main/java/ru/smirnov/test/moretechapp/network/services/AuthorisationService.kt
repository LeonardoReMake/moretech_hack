package ru.smirnov.test.moretechapp.network.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorisationService {

    @POST("backend/login")
    fun login(@Body loginBody: LoginRequest): Call<LoginResponse>

    @POST("backend/register")
    fun register(@Body loginBody: LoginRequest): Call<ResponseBody>

}

data class LoginRequest(
    val username: String?,
    val password: String?
)

data class LoginResponse(
    val token: String
)

data class RegisterResponse(
    val username: String,
    val password: String
)