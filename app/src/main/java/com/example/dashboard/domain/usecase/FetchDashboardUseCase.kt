package com.example.dashboard.domain.usecase

import com.example.dashboard.domain.model.DashboardUiData
import com.example.dashboard.domain.repository.DashboardRepository
import javax.inject.Inject

class FetchDashboardUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(): DashboardUiData {
        val profile = repository.fetchProfile()
        // Can handle Failure case here also by Throwing Exception
        val transactions = repository.fetchTransactions(profile.id)
        val config = repository.fetchConfig()
        return DashboardUiData(profile, transactions, config)
    }
}