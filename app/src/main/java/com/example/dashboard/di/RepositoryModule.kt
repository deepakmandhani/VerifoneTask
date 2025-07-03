package com.example.dashboard.di

import com.example.dashboard.data.api.ApiService
import com.example.dashboard.data.api.FakeApiService
import com.example.dashboard.data.repository.DashboardRepositoryImpl
import com.example.dashboard.domain.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        impl: DashboardRepositoryImpl
    ): DashboardRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        // To simulate errors, replace with below-
//        return FakeApiService(FakeApiService.FailPoint.TRANSACTIONS)
        return FakeApiService()
    }

    @Provides
    @Singleton
    fun bindDashboardRepositoryImpl(
        apiService: ApiService
    ) = DashboardRepositoryImpl(apiService)
}