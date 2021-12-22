package com.example.decotuk_app_capstone.util

import com.example.decotuk_app_capstone.data.source.local.entity.CovidCases
import com.example.decotuk_app_capstone.data.source.local.remote.response.Penambahan
import com.example.decotuk_app_capstone.data.source.local.remote.response.Total

object DataDummy {
    fun getCovidDaily(): CovidCases {
        return CovidCases(
            4500,
            200,
            3500,
            500,
            "2021-12-20",
        )
    }

    fun getCovidTotal(): CovidCases {
        return CovidCases(
            40500,
            2500,
            30500,
            5000,
            "2021-12-21",
        )
    }

    fun generateCovidTotalResponse(): Total {
        return Total(
            positif = 40500,
            dirawat = 2500,
            sembuh = 30500,
            meninggal = 5000,
            lastUpdate = "2021-12-21",
        )
    }

    fun generateCovidDailyResponse(): Penambahan {
        return Penambahan(
            350,
            40500,
            30500,
            5000,
            "",
            "2021-12-21"
        )
    }

}