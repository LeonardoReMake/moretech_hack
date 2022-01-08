package ru.smirnov.test.moretechapp.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.smirnov.test.moretechapp.BuildConfig
import ru.smirnov.test.moretechapp.network.services.AuthorisationService
import ru.smirnov.test.moretechapp.network.services.CalculateCreditService
import ru.smirnov.test.moretechapp.network.services.CarRecognitionService
import ru.smirnov.test.moretechapp.network.services.CarsService
import ru.smirnov.test.moretechapp.network.utils.TokenInterceptor
import javax.inject.Singleton

/**
 * Module for providing objects for DI.
 */
@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .addInterceptor(TokenInterceptor())
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.hostUrl)
            .build()
    }

    @Provides
    fun provideAuthorisationService(retrofit: Retrofit): AuthorisationService
        = retrofit.create(AuthorisationService::class.java)

    @Provides
    fun provideCarsService(retrofit: Retrofit): CarsService
            = retrofit.create(CarsService::class.java)

    @Provides
    fun provideCarRecognitionService(retrofit: Retrofit): CarRecognitionService
            = retrofit.create(CarRecognitionService::class.java)

    @Provides
    fun provideCalculateCreditService(retrofit: Retrofit): CalculateCreditService
            = retrofit.create(CalculateCreditService::class.java)
}