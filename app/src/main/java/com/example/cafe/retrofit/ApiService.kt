package com.example.cafe.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://deallcoffeeshop.xyz/api/"

object ApiService {

    /*val owner: OwnerEndpoint
        get() {
            return retrofit.create(
                OwnerEndpoint::class.java)
        }*/

    val cashier: CashierEndpoint
        get() {
            return retrofit.create(
                CashierEndpoint::class.java)
        }

    val auth: AuthEndpoint
        get() {
            return retrofit.create(
                AuthEndpoint::class.java)
        }

    val retrofit: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
}