package com.example.decotuk_app_capstone.data.source.local.remote

import com.example.decotuk_app_capstone.data.source.local.remote.api.ApiConfig
import com.example.decotuk_app_capstone.data.source.local.remote.response.CovidProvinceResponseItem
import com.example.decotuk_app_capstone.data.source.local.remote.response.Penambahan
import com.example.decotuk_app_capstone.data.source.local.remote.response.Total
import retrofit2.await

class RemoteDataSource {

    companion object {
        private var instance : RemoteDataSource? = null

        fun getInstance() : RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource().apply { instance = this }
            }
    }

    suspend fun getCovidTotal(callback: LoadCovidTotal) {
        ApiConfig.getApiService().getCovidIndonesia().await().total.let {
            callback.onCovidTotalReceived(it)
        }
    }

    suspend fun getCovidDaily(callback: LoadCovidDaily) {
        ApiConfig.getApiService().getCovidIndonesia().await().penambahan.let {
            callback.onCovidDailyReceived(it)
        }
    }

    suspend fun getAllCovidProvinces(callback: LoadCovidProvinces) {
        ApiConfig.getApiService().getCovidProvince().await().let {
            callback.onAllCovidProvincesReceived(it)
        }
    }

    interface LoadCovidTotal {
        fun onCovidTotalReceived(responseCovidTotal: Total)
    }

    interface LoadCovidDaily {
        fun onCovidDailyReceived(responseCovidDaily: Penambahan)
    }

    interface LoadCovidProvinces {
        fun onAllCovidProvincesReceived(responseCovidProvinces: List<CovidProvinceResponseItem>)
    }
}