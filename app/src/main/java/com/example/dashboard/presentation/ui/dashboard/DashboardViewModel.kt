package com.example.dashboard.presentation.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboard.domain.model.DashboardUiData
import com.example.dashboard.domain.usecase.FetchDashboardUseCase
import com.example.dashboard.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val fetchDashboardUseCase: FetchDashboardUseCase
) : ViewModel() {

    private val _dashboardState = MutableStateFlow<Resource<DashboardUiData>>(Resource.Loading)
    val dashboardState: StateFlow<Resource<DashboardUiData>> = _dashboardState

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _dashboardState.value = Resource.Loading
            try {
                val data = fetchDashboardUseCase()
                _dashboardState.value = Resource.Success(data)
            } catch (e: Exception) {
                _dashboardState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
}