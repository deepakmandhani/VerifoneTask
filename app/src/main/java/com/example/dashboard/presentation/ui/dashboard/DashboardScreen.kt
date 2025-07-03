package com.example.dashboard.presentation.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dashboard.R
import com.example.dashboard.domain.model.DashboardUiData
import com.example.dashboard.util.Resource

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state by viewModel.dashboardState.collectAsState()

    when (state) {
        is Resource.Loading -> LoadingView()
        is Resource.Success -> DashboardContent((state as Resource.Success).data)
        is Resource.Error -> ErrorView(
            (state as Resource.Error).message, onRetry = { viewModel.loadDashboard() })
    }
}

@Composable
private fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.error, message), color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

@Composable
private fun DashboardContent(data: DashboardUiData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Welcome, ${data.profile.name}", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(id = R.string.recent_transactions),
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn {
            items(data.transactions) { txn ->
                Text(
                    stringResource(id = R.string.transactions, txn.id, txn.amount.toString()),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Feature Enabled: ${if (data.config.featureEnabled) "Yes" else "No"}",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}