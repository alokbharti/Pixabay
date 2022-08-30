package com.alok.pixabay.domain

import com.alok.pixabay.PixabaySampleDataProvider
import com.alok.pixabay.data.ApiService
import com.alok.pixabay.data.LocalDataSource
import com.alok.pixabay.model.ResponseResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
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
    fun getRepaymentPastPaymentStatement_successfulResponse() = runBlocking {
        // Arrange
        coEvery { localDataSource.getAllPixabayImageData() } returns mutableListOf()
        coEvery {
            apiService.getPixabayImageData(any(), any())
        } returns dataProvider.getSamplePixabayApiValidResponse()

        // Act
        val pixabayImageData = useCaseSUT.getPixabayImageData(query, page)

        // Assert
        // if result is an instance of success class or not
        assertThat(pixabayImageData, instanceOf(ResponseResult.Success::class.java))

        when(pixabayImageData){
            is ResponseResult.Success -> {
                val pixabayImageList = pixabayImageData.data
                assertEquals(pixabayImageList?.size, 1)

                val pixabayImageDetails = pixabayImageList?.get(0)
                assertNotNull(pixabayImageDetails)

                //verifying tags only as this is modified in UseCase class
                assertEquals(pixabayImageDetails!!.tags, "#fruit, #lemon, #art")
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun getRepaymentPastPaymentStatement_errorResponse() = runBlocking {
        // Arrange
        coEvery { localDataSource.getAllPixabayImageData() } returns mutableListOf()
        coEvery {
            apiService.getPixabayImageData(any(), any())
        } returns dataProvider.getSamplePixabayApiCorruptedResponse()

        // Act
        val pixabayImageData = useCaseSUT.getPixabayImageData(query, page)

        // Assert
        // if result is an instance of success class or not
        assertThat(pixabayImageData, instanceOf(ResponseResult.Error::class.java))

        when(pixabayImageData){
            is ResponseResult.Error -> {
                assertEquals(pixabayImageData.errorMessage, "Something went wrong")
            }
        }
    }
}