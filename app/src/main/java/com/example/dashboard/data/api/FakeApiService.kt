package com.example.dashboard.data.api

import com.example.dashboard.data.model.Config
import com.example.dashboard.data.model.Profile
import com.example.dashboard.data.model.Transaction
import kotlinx.coroutines.delay

class FakeApiService(
    private val failAt: FailPoint? = null
) : ApiService {

    enum class FailPoint {
        PROFILE, TRANSACTIONS, CONFIG
    }

    override suspend fun fetchProfile(): Profile {
        delay(1000)
        if (failAt == FailPoint.PROFILE) throw RuntimeException("Failed to load Profile")
        return Profile(id = "verifone_00001", name = "Verifone")
    }

    override suspend fun fetchTransactions(profileId: String): List<Transaction> {
        delay(1000)
        if (failAt == FailPoint.TRANSACTIONS) throw RuntimeException("Failed to load Transactions")
        return listOf(
            Transaction(id = "txn_1", amount = 150.0),
            Transaction(id = "txn_2", amount = 250.5),
            Transaction(id = "txn_3", amount = 1250.5),
            Transaction(id = "txn_4", amount = 50.1),
            Transaction(id = "txn_5", amount = 500.5),
            Transaction(id = "txn_6", amount = 950.0)
        )
    }

    override suspend fun fetchConfig(): Config {
        delay(1000)
        if (failAt == FailPoint.CONFIG) throw RuntimeException("Failed to load Config")
        return Config(featureEnabled = true)
    }
}