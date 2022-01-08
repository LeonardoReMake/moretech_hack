package ru.smirnov.test.moretechapp.network.utils

import okhttp3.Interceptor
import okhttp3.Response
import ru.smirnov.test.moretechapp.CustomApplication

class TokenInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return if (!request.url.toString().contains("api")) {
            chain.proceed(chain.request())
        } else {
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ${CustomApplication.userToken}")
                    .build())
        }
    }
}