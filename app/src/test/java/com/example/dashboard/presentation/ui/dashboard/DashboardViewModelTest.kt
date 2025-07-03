package com.example.dashboard.presentation.ui.dashboard

import com.example.dashboard.domain.usecase.FetchDashboardUseCase
import com.example.dashboard.data.model.Config
import com.example.dashboard.data.model.DashboardData
import com.example.dashboard.data.model.Profile
import com.example.dashboard.data.model.Transaction
import com.example.dashboard.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var fetchDashboardUseCase: FetchDashboardUseCase
    private lateinit var viewModel: DashboardViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fetchDashboardUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should emit Success when fetchDashboardUseCase succeeds`() = runTest {
        // Arrange
        val mockData = DashboardData(
            profile = Profile("1", "John Doe"),
            transactions = listOf(Transaction("t1", 100.0)),
            config = Config(true)
        )
        coEvery { fetchDashboardUseCase() } returns mockData

        // Act
        viewModel = DashboardViewModel(fetchDashboardUseCase)
        advanceUntilIdle()

        // Assert
        val state = viewModel.dashboardState.value
        assertTrue(state is Resource.Success)
        assertEquals(mockData, state.data)
    }

    @Test
    fun `uiState should emit Error when fetchDashboardUseCase throws`() = runTest {
        // Arrange
        coEvery { fetchDashboardUseCase() } throws RuntimeException("Something went wrong")

        // Act
        viewModel = DashboardViewModel(fetchDashboardUseCase)
        advanceUntilIdle()

        // Assert
        val state = viewModel.dashboardState.value
        assertTrue(state is Resource.Error)
        assertEquals("Something went wrong", state.message)
    }

    @Test
    fun `uiState should initially be Loading`() = runTest {
        // Arrange
        coEvery { fetchDashboardUseCase() } coAnswers {
            delay(100) // simulate loading delay
            DashboardData(Profile("1", "Test"), emptyList(), Config(true))
        }

        // Act
        viewModel = DashboardViewModel(fetchDashboardUseCase)

        // Assert
        val state = viewModel.dashboardState.value
        assertTrue(state is Resource.Loading)
    }
}