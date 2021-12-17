package com.example.decotuk_app_capstone.data.source.local.entity

data class CovidCases(
    var confirmed: Int,
    var hospitalize: Int,
    var recovery: Int,
    var death: Int,
    var date: String,
    var province: String
)
