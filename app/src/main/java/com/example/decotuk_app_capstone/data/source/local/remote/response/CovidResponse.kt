package com.example.decotuk_app_capstone.data.source.local.remote.response

import com.google.gson.annotations.SerializedName

data class CovidResponse(

	@field:SerializedName("penambahan")
	val penambahan: Penambahan,

	@field:SerializedName("total")
	val total: Total,

	@field:SerializedName("data")
	val data: Data
)

data class Data(

	@field:SerializedName("total_spesimen")
	val totalSpesimen: Int,

	@field:SerializedName("total_spesimen_negatif")
	val totalSpesimenNegatif: Int,

	@field:SerializedName("odp")
	val odp: Int,

	@field:SerializedName("pdp")
	val pdp: Int
)

data class Penambahan(

	@field:SerializedName("meninggal")
	val meninggal: Int,

	@field:SerializedName("positif")
	val positif: Int,

	@field:SerializedName("sembuh")
	val sembuh: Int,

	@field:SerializedName("dirawat")
	val dirawat: Int,

	@field:SerializedName("created")
	val created: String,

	@field:SerializedName("tanggal")
	val tanggal: String
)

data class Total(

	@field:SerializedName("meninggal")
	val meninggal: Int,

	@field:SerializedName("positif")
	val positif: Int,

	@field:SerializedName("sembuh")
	val sembuh: Int,

	@field:SerializedName("dirawat")
	val dirawat: Int,

	@field:SerializedName("lastUpdate")
	val lastUpdate: String
)
