package com.example.famlistapp.ui.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.famlistapp.data.model.ShoppingItem
import com.example.famlistapp.data.model.ShoppingItemPriority
import com.example.famlistapp.ui.theme.FamListAppTheme
// import com.example.famlistapp.ui.screens.history.viewmodel.ShoppingHistoryViewModel // Conceptual import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingHistoryScreen(
    // viewModel: ShoppingHistoryViewModel, // Injected by Hilt or similar
    onNavigateBack: () -> Unit,
    historyItems: List<ShoppingItem> // Passed from ViewModel state for preview
) {
    // val uiState by viewModel.uiState.collectAsState() // Example of collecting state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("购物历史") }, // Shopping History
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "返回") // Back
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (historyItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("还没有购物历史记录。", style = MaterialTheme.typography.bodyLarge) // No shopping history yet.
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(historyItems, key = { it.id }) { item ->
                        HistoryItemRow(item = item)
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemRow(item: ShoppingItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${item.priority.firstOrNull() ?: ""} ${item.name}", // Show priority emoji
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "购买者: ${item.addedByUserName}", // Bought by:
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            item.boughtTimestamp?.let {
                Text(
                    text = formatBoughtTimestamp(it),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}

fun formatBoughtTimestamp(timestamp: Long): String {
    // Simple formatting, replace with more sophisticated date formatting
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun ShoppingHistoryScreenPreview() {
    FamListAppTheme {
        val sampleHistoryItems = listOf(
            ShoppingItem(id = "1", name = "牛奶", addedByUserId = "user1", addedByUserName = "爸爸", isBought = true, boughtTimestamp = System.currentTimeMillis() - 100000000, priority = ShoppingItemPriority.NORMAL.displayName),
            ShoppingItem(id = "2", name = "鸡蛋 (非常非常非常长的一个名字来测试省略号)", addedByUserId = "user2", addedByUserName = "妈妈", isBought = true, boughtTimestamp = System.currentTimeMillis() - 200000000, priority = ShoppingItemPriority.URGENT.displayName),
            ShoppingItem(id = "3", name = "面包", addedByUserId = "user1", addedByUserName = "爸爸", isBought = true, boughtTimestamp = System.currentTimeMillis(), priority = ShoppingItemPriority.LOW.displayName)
        )
        ShoppingHistoryScreen(
            onNavigateBack = {},
            historyItems = sampleHistoryItems
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingHistoryScreenEmptyPreview() {
    FamListAppTheme {
        ShoppingHistoryScreen(
            onNavigateBack = {},
            historyItems = emptyList()
        )
    }
}
