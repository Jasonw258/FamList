package com.example.famlistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.famlistapp.ui.theme.FamListAppTheme
import com.example.famlistapp.ui.screens.onboarding.OnboardingScreen
import com.example.famlistapp.ui.screens.onboarding.CreateFamilyScreen
import com.example.famlistapp.ui.screens.onboarding.JoinFamilyScreen
import com.example.famlistapp.ui.screens.shoppinglist.ShoppingListScreen
// import com.example.famlistapp.ui.screens.shoppinglist.viewmodel.ShoppingListViewModel // Conceptually
import com.example.famlistapp.ui.screens.history.ShoppingHistoryScreen // Import History Screen
// For a real app, you'd use Hilt or another DI framework for ViewModels
// import androidx.lifecycle.viewmodel.compose.viewModel
// Assume other ViewModels (like Onboarding ones) are handled similarly or via composable-level instantiation for simulation

// Regarding Locale Changes & Recomposition:
// When the system locale changes (e.g., user changes language in device settings),
// Android typically recreates the Activity. This means MainActivity's onCreate will be called again.
// Jetpack Compose will then read the new string resources based on the updated locale
// during recomposition, automatically updating the UI with translated strings.
// For dynamic in-app language switching without recreating the Activity,
// you'd need a more complex setup involving a CompositionLocalProvider for locale
// and manually triggering recomposition when the app's language setting changes.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FamListAppTheme {
                FamListApp()
            }
        }
    }
}

@Composable
fun FamListApp() {
    var currentScreen by remember { mutableStateOf(Screen.Onboarding) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (currentScreen) {
            Screen.Onboarding -> OnboardingScreen(
                onCreateFamilyClicked = { currentScreen = Screen.CreateFamily },
                onJoinFamilyClicked = { currentScreen = Screen.JoinFamily }
            )
            Screen.CreateFamily -> CreateFamilyScreen(
                onFamilyCreated = {
                    // In a real app, you'd get the familyId from the ViewModel/SupabaseClient result
                    // and pass it to the ShoppingListScreen or its ViewModel.
                    // For now, we'll just switch the screen.
                    currentScreen = Screen.ShoppingList
                }
            )
            Screen.JoinFamily -> JoinFamilyScreen(
                onJoinFamilyClicked = { _, _ ->
                    // Similar to CreateFamily, familyId would be involved.
                    currentScreen = Screen.ShoppingList
                }
            )
            Screen.ShoppingList -> {
                // In a real app, you'd obtain the ViewModel instance,
                // possibly using `viewModel<ShoppingListViewModel>()`.
                // For this conceptual update, we pass dummy data and lambdas.
                // val shoppingListViewModel: ShoppingListViewModel = viewModel() // Example
                // val uiState by shoppingListViewModel.uiState.collectAsState()
                // ShoppingListScreen(
                //     items = uiState.items,
                //     onAddItem = { itemName -> shoppingListViewModel.addItem(itemName) },
                //     onToggleItemBought = { item -> shoppingListViewModel.toggleItemBought(item) },
                //     onDeleteItem = { item -> shoppingListViewModel.deleteItem(item.id) }
                // )

                // Conceptual placeholder call without actual ViewModel instance:
                ShoppingListScreen(
                    groupedItems = emptyList(),  // 使用正确的参数名
                    onAddItem = { name, category, quantity, unit, notes, priority -> 
                        // TODO: 在这里处理添加项目的逻辑
                    },
                    onToggleItemBought = {},
                    onDeleteItem = {},
                    onVoiceInputClicked = {},
                    onClearBoughtItemsClicked = {
                        // shoppingListViewModel.onClearBoughtItemsClicked() // Example call
                        println("MainActivity: Clear Bought Items Clicked")
                    },
                    onViewHistoryClicked = { currentScreen = Screen.ShoppingHistory } // Navigate to History
                )
            }
            Screen.ShoppingHistory -> {
                // In a real app, obtain ShoppingHistoryViewModel instance
                // val historyViewModel: ShoppingHistoryViewModel = viewModel()
                // val historyState by historyViewModel.uiState.collectAsState()
                ShoppingHistoryScreen(
                    // historyItems = historyState.boughtItems,
                    historyItems = emptyList(), // Dummy data for preview
                    onNavigateBack = { currentScreen = Screen.ShoppingList } // Navigate back
                )
            }
        }
    }
}

enum class Screen {
    Onboarding,
    CreateFamily,
    JoinFamily,
    ShoppingList,
    ShoppingHistory // Added ShoppingHistory screen
    // SettingsScreen
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FamListAppTheme {
        FamListApp() // This will show Onboarding by default
    }
}
