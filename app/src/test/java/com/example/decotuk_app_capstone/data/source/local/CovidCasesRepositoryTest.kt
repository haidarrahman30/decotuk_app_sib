package com.example.decotuk_app_capstone.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.decotuk_app_capstone.data.source.local.remote.RemoteDataSource
import com.example.decotuk_app_capstone.util.DataDummy
import com.example.decotuk_app_capstone.utils.LiveDataTestUtils
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class CovidCasesRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = Mockito.mock(RemoteDataSource::class.java)
    private val covidCasesRepository = FakeCovidCasesRepositoryTest(remote)

    private val covidTotalResponse = DataDummy.generateCovidTotalResponse()
    private val covidDailyResponse = DataDummy.generateCovidDailyResponse()

    @Test
    fun testGetCovidTotal() {
        runBlocking {
            doAnswer {
                (it.arguments[0] as RemoteDataSource.LoadCovidTotal).onCovidTotalReceived(covidTotalResponse)
                null
            }.`when`(remote).getCovidTotal(any())
        }

        val covidTotalCase = LiveDataTestUtils.getValues(covidCasesRepository.getCovidTotal())
        runBlocking {
            verify(remote).getCovidTotal(any())
        }
        assertNotNull(covidTotalCase)
        assertEquals(covidTotalResponse.positif, covidTotalCase.confirmed)
    }

    @Test
    fun testGetCovidDaily() {
        runBlocking {
            doAnswer {
                (it.arguments[0] as RemoteDataSource.LoadCovidDaily).onCovidDailyReceived(covidDailyResponse)
                null
            }.`when`(remote).getCovidDaily(any())
        }

        val covidDailyCase = LiveDataTestUtils.getValues(covidCasesRepository.getCovidDaily())
        runBlocking {
            verify(remote).getCovidDaily(any())
        }
        assertNotNull(covidDailyCase)
        assertEquals(covidDailyResponse.positif, covidDailyCase.confirmed)
    }
}