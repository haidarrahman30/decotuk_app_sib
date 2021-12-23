package com.example.decotuk_app_capstone.ui.covid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.decotuk_app_capstone.data.source.local.CovidCasesRepository
import com.example.decotuk_app_capstone.data.source.local.entity.CovidCases
import com.example.decotuk_app_capstone.util.DataDummy
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CovidStatViewModelTest {

    private lateinit var viewModel: CovidStatViewModel
    private var dataCasesDaily = DataDummy.getCovidDaily()
    private var dataCasesTotal = DataDummy.getCovidTotal()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var covidCasesRepository: CovidCasesRepository

    @Mock
    private lateinit var observer: Observer<CovidCases>

    @Before
    fun setUp(){
        viewModel = CovidStatViewModel(covidCasesRepository)

    }

    @Test
    fun testGetCovidDaily() {
        val cases = MutableLiveData<CovidCases>()
        cases.value = dataCasesDaily

        `when`(covidCasesRepository.getCovidDaily()).thenReturn(cases)
        val case = viewModel.getCovidDaily().value as CovidCases
        verify(covidCasesRepository).getCovidDaily()

        assertNotNull(case)
        assertEquals(dataCasesDaily.confirmed, case.confirmed)
        assertEquals(dataCasesDaily.hospitalize, case.hospitalize)
        assertEquals(dataCasesDaily.recovery, case.recovery)
        assertEquals(dataCasesDaily.death, case.death)
        assertEquals(dataCasesDaily.confirmed, case.confirmed)
        assertEquals(dataCasesDaily.date, case.date)

        viewModel.getCovidDaily().observeForever(observer)
        verify(observer).onChanged(dataCasesDaily)
    }

    @Test
    fun testGetCovidTotal() {
        val cases = MutableLiveData<CovidCases>()
        cases.value = dataCasesTotal

        `when`(covidCasesRepository.getCovidTotal()).thenReturn(cases)
        val case = viewModel.getCovidTotal().value as CovidCases
        verify(covidCasesRepository).getCovidTotal()

        assertNotNull(case)
        assertEquals(dataCasesTotal.confirmed, case.confirmed)
        assertEquals(dataCasesTotal.hospitalize, case.hospitalize)
        assertEquals(dataCasesTotal.recovery, case.recovery)
        assertEquals(dataCasesTotal.death, case.death)
        assertEquals(dataCasesTotal.confirmed, case.confirmed)
        assertEquals(dataCasesTotal.date, case.date)

        viewModel.getCovidTotal().observeForever(observer)
        verify(observer).onChanged(dataCasesTotal)
    }
}