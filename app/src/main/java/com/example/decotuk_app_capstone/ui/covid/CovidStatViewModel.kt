package com.example.decotuk_app_capstone.ui.covid

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.decotuk_app_capstone.data.source.local.CovidCasesRepository
import com.example.decotuk_app_capstone.data.source.local.entity.CovidCases

class CovidStatViewModel (private val covidCasesRepository: CovidCasesRepository) : ViewModel() {

    fun getCovidTotal() : LiveData<CovidCases> = covidCasesRepository.getCovidTotal()

    fun getCovidDaily() : LiveData<CovidCases> = covidCasesRepository.getCovidDaily()

    fun getCovidProvinces() : LiveData<List<CovidCases>> = covidCasesRepository.getAllCovidProvinces()
}