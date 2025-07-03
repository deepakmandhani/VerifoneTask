package com.example.dashboard.domain.repository

import com.example.dashboard.data.model.Config
import com.example.dashboard.data.model.Profile
import com.example.dashboard.data.model.Transaction

interface DashboardRepository {
    suspend fun fetchProfile(): Profile
    suspend fun fetchTransactions(profileId: String): List<Transaction>
    suspend fun fetchConfig(): Config
}