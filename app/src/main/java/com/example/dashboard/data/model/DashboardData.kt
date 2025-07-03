package com.example.dashboard.data.model

data class DashboardData(
    val profile: Profile,
    val transactions: List<Transaction>,
    val config: Config
)