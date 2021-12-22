package com.example.decotuk_app_capstone.ui.covid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decotuk_app_capstone.R
import com.example.decotuk_app_capstone.data.source.local.entity.CovidCases
import com.example.decotuk_app_capstone.databinding.ContentCovid19StatBinding
import com.example.decotuk_app_capstone.databinding.FragmentCovidStatBinding
import com.example.decotuk_app_capstone.util.ViewModelFactory

class CovidStatFragment : Fragment() {

    private lateinit var fragmentCovidBinding: FragmentCovidStatBinding
    private lateinit var contentCovidBinding: ContentCovid19StatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentCovidBinding = FragmentCovidStatBinding.inflate(layoutInflater, container, false)
        contentCovidBinding = fragmentCovidBinding.detailContentCovid
        return fragmentCovidBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireActivity())
            val viewModel = ViewModelProvider(this, factory)[CovidStatViewModel::class.java]

            //get covid daily
            showProgressBar()
            viewModel.getCovidDaily().observe(viewLifecycleOwner, {
                hideProgressBar()
                populateCovidDaily(it)
            })

            //get covid total
            viewModel.getCovidTotal().observe(viewLifecycleOwner, {
                populateCovidTotal(it)
            })

        }
    }

    private fun populateCovidDaily(covidCases: CovidCases) {
        with(contentCovidBinding){
            tvConfirmed.text = numberFormat(covidCases.confirmed)
            tvRecovery.text = numberFormat(covidCases.recovery)
            tvDeath.text = numberFormat(covidCases.death)
            todayDate.text = resources.getString(R.string.today_case, covidCases.date)
        }
    }

    private fun populateCovidTotal(covidCases: CovidCases) {
        with(contentCovidBinding){
            tvTotalCase.text = numberFormat(covidCases.confirmed)
            tvTotalRecovery.text = numberFormat(covidCases.recovery)
            tvTotalDeath.text = numberFormat(covidCases.death)
        }
    }

    private fun showProgressBar() {
        fragmentCovidBinding.progressBar.visibility = View.VISIBLE
        fragmentCovidBinding.content.visibility = View.INVISIBLE
    }

    private fun hideProgressBar() {
        fragmentCovidBinding.progressBar.visibility = View.GONE
        fragmentCovidBinding.content.visibility = View.VISIBLE
    }

    private fun numberFormat(input: Int) : String {
        return "%,d".format(input)
    }

}