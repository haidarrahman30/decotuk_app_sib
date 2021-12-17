package com.example.decotuk_app_capstone.data.source.local.remote.response

import com.google.gson.annotations.SerializedName

data class CovidProvinceResponse(

	@field:SerializedName("CovidProvinceResponse")
	val covidProvinceResponse: List<CovidProvinceResponseItem>
)

data class CovidProvinceResponseItem(

	@field:SerializedName("provinsi")
	val provinsi: String,

	@field:SerializedName("meninggal")
	val meninggal: Int,

	@field:SerializedName("sembuh")
	val sembuh: Int,

	@field:SerializedName("dirawat")
	val dirawat: Int,

	@field:SerializedName("kasus")
	val kasus: Int
)
