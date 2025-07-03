package com.example.dashboard.presentation.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.dashboard.data.model.Config
import com.example.dashboard.data.model.Transaction
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
fun DashboardContent(data: DashboardUiData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Greeting Header
        Text(
            text = "Welcome, ${data.profile.name}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Config Info
        FeatureConfigCard(data.config)

        Spacer(modifier = Modifier.height(24.dp))

        // Transactions Title
        Text(
            text = stringResource(id = R.string.recent_transactions),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Transactions List
        if (data.transactions.isEmpty()) {
            Text(
                text = "No recent transactions",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(data.transactions) { txn ->
                    TransactionItem(txn)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(txn: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                stringResource(id = R.string.transactions, txn.id),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                stringResource(id = R.string.amounts, txn.amount),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun FeatureConfigCard(config: Config) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.status),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = if (config.featureEnabled) "Enabled ✅" else "Disabled ❌",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Language: ${config.language}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

