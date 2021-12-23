package com.example.decotuk_app_capstone.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.decotuk_app_capstone.data.source.local.entity.CovidCases
import com.example.decotuk_app_capstone.data.source.local.remote.RemoteDataSource
import com.example.decotuk_app_capstone.data.source.local.remote.response.Penambahan
import com.example.decotuk_app_capstone.data.source.local.remote.response.Total
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FakeCovidCasesRepositoryTest (private val remoteDataSource: RemoteDataSource) : CovidCasesDataSource {

    override fun getCovidTotal(): LiveData<CovidCases> {
        val dataCovid = MutableLiveData<CovidCases>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getCovidTotal(object : RemoteDataSource.LoadCovidTotal {
                override fun onCovidTotalReceived(responseCovidTotal: Total) {
                    val covidCase = CovidCases(
                        confirmed = responseCovidTotal.positif,
                        hospitalize = responseCovidTotal.dirawat,
                        recovery = responseCovidTotal.sembuh,
                        death = responseCovidTotal.meninggal,
                        date = responseCovidTotal.lastUpdate,
                    )
                    dataCovid.postValue(covidCase)
                }
            })
        }
        return dataCovid
    }

    override fun getCovidDaily(): LiveData<CovidCases> {
        val dataCovidDaily = MutableLiveData<CovidCases>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getCovidDaily(object : RemoteDataSource.LoadCovidDaily {
                override fun onCovidDailyReceived(responseCovidDaily: Penambahan) {
                    val covidCaseDaily = CovidCases(
                        confirmed = responseCovidDaily.positif,
                        hospitalize = responseCovidDaily.dirawat,
                        recovery = responseCovidDaily.sembuh,
                        death = responseCovidDaily.meninggal,
                        date = responseCovidDaily.tanggal,
                    )
                    dataCovidDaily.postValue(covidCaseDaily)
                }
            })
        }
        return dataCovidDaily
    }
}