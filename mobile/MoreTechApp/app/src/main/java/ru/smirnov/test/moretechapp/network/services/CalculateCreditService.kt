package ru.smirnov.test.moretechapp.network.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CalculateCreditService {

    @POST("backend/api/v2/credits/calculate")
    fun calculate(@Body creditInfo: CreditCalculateRequest): Call<CreditCalculateResponse>

}

data class CreditCalculateRequest(
    val loanAmount: Double,
    val creditTerm: Int
)

data class CreditCalculateResponse(
    val loanAmount: Double,
    val monthPayment: Double,
    val fullPayment: Double,
    val overpayment: Double,
    val creditTerm: Double,
    val rate: Double
)