package com.example.decotuk_app_capstone.ui.covid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.decotuk_app_capstone.data.source.local.entity.CovidCases
import com.example.decotuk_app_capstone.databinding.ItemsCovidProvinceBinding

class CovidStatProvinceAdapter : RecyclerView.Adapter<CovidStatProvinceAdapter.CovidStatViewHolder>() {

    val listCovidCasesProvince = ArrayList<CovidCases>()

    fun setCovidCases(caseProvinces : List<CovidCases>?) {
        if (caseProvinces == null) return
        this.listCovidCasesProvince.clear()
        this.listCovidCasesProvince.addAll(caseProvinces)
    }
    inner class CovidStatViewHolder(private val binding: ItemsCovidProvinceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(covidCase: CovidCases) {
            with(binding) {
                tvProvince.text = covidCase.province
                tvProvinceTotalCase.text = covidCase.confirmed.toString()
                tvProvinceHospitalization.text = covidCase.hospitalize.toString()
                tvProvinceTotalDeath.text = covidCase.death.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CovidStatViewHolder {
        val itemsCovidProvinceBinding = ItemsCovidProvinceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CovidStatViewHolder(itemsCovidProvinceBinding)
    }

    override fun onBindViewHolder(holder: CovidStatViewHolder, position: Int) {
        val covidCases = listCovidCasesProvince[position]
        holder.bind(covidCases)
    }

    override fun getItemCount(): Int = listCovidCasesProvince.size
}