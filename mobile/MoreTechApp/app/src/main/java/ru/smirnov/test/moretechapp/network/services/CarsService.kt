package ru.smirnov.test.moretechapp.network.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.smirnov.test.moretechapp.models.Car

interface CarsService {

    @GET("backend/api/v2/marketplace")
    fun getAllCars(): Call<List<Car>>

    @GET("backend/api/v1/cars/{id}")
    fun getCarById(@Path("id") carId: String): Call<CarByIdResponse>

}

data class AllCarResponse(
    val cars: List<Car>
)

data class CarByIdResponse(
    val car: Car
)