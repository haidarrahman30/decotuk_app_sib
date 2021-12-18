package com.example.decotuk_app_capstone.data.di

import android.content.Context
import com.example.decotuk_app_capstone.data.source.local.CovidCasesRepository
import com.example.decotuk_app_capstone.data.source.local.remote.RemoteDataSource

object Injection {
    fun provideRepository(context: Context) : CovidCasesRepository {
        val remoteDataSource = RemoteDataSource.getInstance()

        return CovidCasesRepository.getInstace(remoteDataSource)
    }
}