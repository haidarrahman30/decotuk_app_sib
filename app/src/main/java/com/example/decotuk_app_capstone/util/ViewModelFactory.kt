package com.example.decotuk_app_capstone.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.decotuk_app_capstone.data.di.Injection
import com.example.decotuk_app_capstone.data.source.local.CovidCasesRepository
import com.example.decotuk_app_capstone.ui.covid.CovidStatViewModel

class ViewModelFactory private constructor(private val covidRepository: CovidCasesRepository): ViewModelProvider.NewInstanceFactory(){
    companion object{
        @Volatile
        private var instance : ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
                    .apply { instance = this }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when{
            modelClass.isAssignableFrom(CovidStatViewModel::class.java) -> {
                return CovidStatViewModel(covidRepository) as T
            }
            else -> {
                throw Throwable("Unknown ViewModel class" + modelClass)
            }
        }
    }
}