package com.alok.pixabay.domain

import com.alok.pixabay.PixabaySampleDataProvider
import com.alok.pixabay.data.ApiService
import com.alok.pixabay.data.LocalDataSource
import com.alok.pixabay.model.ResponseResult
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PixabayUsecaseTest{

    //Sample data for testing
    private val query = "fruits"
    private val page = 1

    //system under test
    private lateinit var useCaseSUT:PixabayUsecase

    @MockK
    private lateinit var apiService: ApiService
    @MockK
    private lateinit var localDataSource: LocalDataSource
    private lateinit var dataProvider: PixabaySampleDataProvider

    @Before
    fun setUpRepaymentPastPaymentUseCase() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCaseSUT = spyk(PixabayUsecase(apiService, localDataSource))
        dataProvider = PixabaySampleDataProvider()
    }

    @Test
    @Throws(Exception::class)
    fun getPixabayImageData_successfulResponse() = runBlocking {
        // Arrange
        coEvery { localDataSource.getAllPixabayImageData() } returns mutableListOf()
        coEvery {
            apiService.getPixabayImageData(any(), any())
        } returns dataProvider.getSamplePixabayApiValidResponse()

        // Act
        val pixabayImageData = useCaseSUT.getPixabayImageData(query, page)

        // Assert
        assertThat(pixabayImageData, instanceOf(ResponseResult.Success::class.java))

        when(pixabayImageData) {
            is ResponseResult.Success -> {
                val pixabayImageList = pixabayImageData.data
                assertEquals(pixabayImageList?.size, 1)

                val pixabayImageDetails = pixabayImageList?.get(0)
                assertNotNull(pixabayImageDetails)

                //verifying tags only as this is modified in UseCase class
                assertEquals(pixabayImageDetails!!.tags, "#fruit, #lemon, #art")

                //verifying 3 functions of LocalDataSource should be called
                //to store/update the data locally
                coVerify(exactly = 1) { localDataSource.deleteExistingPixabayImageData() }
                coVerify(exactly = 1) { localDataSource.insertPixabayImageDetailsList(any()) }
                coVerify(exactly = 1) { localDataSource.saveLastSearchedQueryToSharedPref(any()) }
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun getPixabayImageData_errorResponse() = runBlocking {
        // Arrange
        coEvery { localDataSource.getAllPixabayImageData() } returns mutableListOf()
        coEvery {
            apiService.getPixabayImageData(any(), any())
        } returns dataProvider.getSamplePixabayApiCorruptedResponse()

        // Act
        val pixabayImageData = useCaseSUT.getPixabayImageData(query, page)

        // Assert
        assertThat(pixabayImageData, instanceOf(ResponseResult.Error::class.java))

        when(pixabayImageData){
            is ResponseResult.Error -> {
                //verifying 2 functions of LocalDataSource should be called
                //to get data locally
                coVerify(exactly = 1) { localDataSource.getAllPixabayImageData() }
                //since no data in db, it shouldn't be invoked
                coVerify(exactly = 0) { localDataSource.getLastSearchedQueryFromSharedPref() }

                assertEquals(pixabayImageData.errorMessage, "Something went wrong")
            }
        }
    }
}