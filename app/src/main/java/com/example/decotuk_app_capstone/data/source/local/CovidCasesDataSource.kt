package com.example.decotuk_app_capstone.data.source.local

import androidx.lifecycle.LiveData
import com.example.decotuk_app_capstone.data.source.local.entity.CovidCases

interface CovidCasesDataSource {
    fun getCovidTotal() : LiveData<CovidCases>

    fun getCovidDaily() : LiveData<CovidCases>
}