import com.example.dashboard.data.model.Config
import com.example.dashboard.data.model.Profile
import com.example.dashboard.data.model.Transaction
import com.example.dashboard.domain.model.DashboardUiData
import com.example.dashboard.domain.repository.DashboardRepository
import com.example.dashboard.domain.usecase.FetchDashboardUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class FetchDashboardUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: DashboardRepository
    private lateinit var useCase: FetchDashboardUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        useCase = FetchDashboardUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return DashboardData when all API calls succeed`() = runTest {
        // Arrange
        val profile = Profile("1", "Test User")
        val transactions = listOf(Transaction("tx1", 100.0))
        val config = Config( true)

        coEvery { repository.fetchProfile() } returns profile
        coEvery { repository.fetchTransactions("1") } returns transactions
        coEvery { repository.fetchConfig() } returns config

        // Act
        val result = useCase()

        // Assert
        assertEquals(DashboardUiData(profile, transactions, config), result)
    }

    @Test
    fun `should throw exception if profile fetch fails`() = runTest {
        coEvery { repository.fetchProfile() } throws RuntimeException("Profile failed")

        assertFailsWith<RuntimeException> {
            useCase()
        }
    }

    @Test
    fun `should throw exception if transactions fetch fails`() = runTest {
        val profile = Profile("1", "Test User")

        coEvery { repository.fetchProfile() } returns profile
        coEvery { repository.fetchTransactions("1") } throws RuntimeException("Transactions failed")

        assertFailsWith<RuntimeException> {
            useCase()
        }
    }

    @Test
    fun `should throw exception if config fetch fails`() = runTest {
        val profile = Profile("1", "Test User")
        val transactions = listOf(Transaction("tx1", 100.0))

        coEvery { repository.fetchProfile() } returns profile
        coEvery { repository.fetchTransactions("1") } returns transactions
        coEvery { repository.fetchConfig() } throws RuntimeException("Config failed")

        assertFailsWith<RuntimeException> {
            useCase()
        }
    }
}