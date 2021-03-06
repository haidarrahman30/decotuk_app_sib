package com.example.decotuk_app_capstone.data.source.local.remote.api

import com.example.decotuk_app_capstone.data.source.local.remote.response.CovidResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("more")
    fun getCovidIndonesia(): Call<CovidResponse>
}