package com.example.decotuk_app_capstone.data.source.local.remote

import com.example.decotuk_app_capstone.data.source.local.remote.api.ApiConfig
import com.example.decotuk_app_capstone.data.source.local.remote.response.Penambahan
import com.example.decotuk_app_capstone.data.source.local.remote.response.Total
import com.example.decotuk_app_capstone.util.EspressoIdlingResource
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
        EspressoIdlingResource.increment()
        ApiConfig.getApiService().getCovidIndonesia().await().total.let {
            callback.onCovidTotalReceived(it)
            EspressoIdlingResource.decrement()
        }
    }

    suspend fun getCovidDaily(callback: LoadCovidDaily) {
        EspressoIdlingResource.increment()
        ApiConfig.getApiService().getCovidIndonesia().await().penambahan.let {
            callback.onCovidDailyReceived(it)
            EspressoIdlingResource.decrement()
        }
    }

    interface LoadCovidTotal {
        fun onCovidTotalReceived(responseCovidTotal: Total)
    }

    interface LoadCovidDaily {
        fun onCovidDailyReceived(responseCovidDaily: Penambahan)
    }

}