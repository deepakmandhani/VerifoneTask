package com.example.dashboard.data.repository

import com.example.dashboard.data.api.ApiService
import com.example.dashboard.data.model.Config
import com.example.dashboard.data.model.Profile
import com.example.dashboard.data.model.Transaction
import com.example.dashboard.domain.repository.DashboardRepository

class DashboardRepositoryImpl(
    private val apiService: ApiService
) : DashboardRepository {

    override suspend fun fetchProfile(): Profile {
        return apiService.fetchProfile()
    }

    override suspend fun fetchTransactions(profileId: String): List<Transaction> {
        return apiService.fetchTransactions(profileId)
    }

    override suspend fun fetchConfig(): Config {
        return apiService.fetchConfig()
    }
}