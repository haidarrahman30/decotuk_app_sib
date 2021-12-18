package com.example.decotuk_app_capstone.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.decotuk_app_capstone.data.source.local.entity.CovidCases
import com.example.decotuk_app_capstone.data.source.local.remote.RemoteDataSource
import com.example.decotuk_app_capstone.data.source.local.remote.response.CovidProvinceResponseItem
import com.example.decotuk_app_capstone.data.source.local.remote.response.Penambahan
import com.example.decotuk_app_capstone.data.source.local.remote.response.Total
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CovidCasesRepository private constructor(private val remoteDataSource: RemoteDataSource) : CovidCasesDataSource {
    companion object {
        private var instance : CovidCasesRepository? = null

        fun getInstace(remoteDataSource: RemoteDataSource) : CovidCasesRepository =
            instance ?: synchronized(this){
                instance ?: CovidCasesRepository(remoteDataSource).apply { instance = this }
            }

    }

    override fun getCovidTotal(): LiveData<CovidCases> {
        val dataCovid = MutableLiveData<CovidCases>()
        CoroutineScope(IO).launch {
            remoteDataSource.getCovidTotal(object : RemoteDataSource.LoadCovidTotal {
                override fun onCovidTotalReceived(responseCovidTotal: Total) {
                    val covidCase = CovidCases(
                        confirmed = responseCovidTotal.positif,
                        hospitalize = responseCovidTotal.dirawat,
                        recovery = responseCovidTotal.sembuh,
                        death = responseCovidTotal.meninggal,
                        date = responseCovidTotal.lastUpdate,
                        province = ""
                    )
                    dataCovid.postValue(covidCase)
                }
            })
        }
        return dataCovid
    }

    override fun getCovidDaily(): LiveData<CovidCases> {
        val dataCovidDaily = MutableLiveData<CovidCases>()
        CoroutineScope(IO).launch {
            remoteDataSource.getCovidDaily(object : RemoteDataSource.LoadCovidDaily {
                override fun onCovidDailyReceived(responseCovidDaily: Penambahan) {
                    val covidCaseDaily = CovidCases(
                        confirmed = responseCovidDaily.positif,
                        hospitalize = responseCovidDaily.dirawat,
                        recovery = responseCovidDaily.sembuh,
                        death = responseCovidDaily.meninggal,
                        date = responseCovidDaily.tanggal,
                        province = ""
                    )
                    dataCovidDaily.postValue(covidCaseDaily)
                }
            })
        }
        return dataCovidDaily
    }

    override fun getAllCovidProvinces(): LiveData<List<CovidCases>> {
        val covidProvinces = MutableLiveData<List<CovidCases>>()
        CoroutineScope(IO).launch {
            remoteDataSource.getAllCovidProvinces(object : RemoteDataSource.LoadCovidProvinces {
                override fun onAllCovidProvincesReceived(responseCovidProvinces: List<CovidProvinceResponseItem>) {
                    val covidData = ArrayList<CovidCases>()
                    for (i in responseCovidProvinces) {
                        val covid = CovidCases(
                            confirmed = i.kasus,
                            hospitalize = i.dirawat,
                            recovery = i.sembuh,
                            death = i.meninggal,
                            date = "",
                            province = i.provinsi
                        )
                        covidData.add(covid)
                    }
                    covidProvinces.postValue(covidData)
                }
            })
        }
        return covidProvinces
    }
}