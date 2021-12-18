package com.example.decotuk_app_capstone.data.source.local.entity

data class User(
    var id : String? = null,
    var email : String? = null,
    var nama : String? = null,
    var password : String? = null,
    var image : String? = null,
    var rekam : Any? = null,
    var diagnosa : Diagnosa? = null,
    var laporan : Any? = null,
    var status : Int = 0,
)
