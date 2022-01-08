package ru.smirnov.test.moretechapp.network.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CarRecognitionService {

    @POST("backend/api/v2/recognition")
    fun recognize(@Body content: RecognizeRequest): Call<ResponseBody>

}

data class RecognizeRequest(val content: String)