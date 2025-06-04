package com.example.famlistapp.ui.screens.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.famlistapp.data.ShoppingRepository
import com.example.famlistapp.data.model.ShoppingItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ShoppingHistoryUiState(
    val boughtItems: List<ShoppingItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentFamilyId: String? = "dummyFamilyId" // Example family ID
)

class ShoppingHistoryViewModel(
    private val repository: ShoppingRepository
    // private val savedStateHandle: SavedStateHandle // For receiving navigation arguments like familyId
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingHistoryUiState())
    val uiState: StateFlow<ShoppingHistoryUiState> = _uiState

    private val familyId: String?
        get() = _uiState.value.currentFamilyId // In real app, get from SavedStateHandle or similar

    init {
        // Load history when ViewModel is created and familyId is available
        // familyId = savedStateHandle.get<String>("familyId") // Example
        loadShoppingHistory()
    }

    fun loadShoppingHistory() {
        val currentFamId = familyId
        if (currentFamId == null || currentFamId.isBlank() || currentFamId == "dummyFamilyId") {
            _uiState.value = _uiState.value.copy(
                error = "Family ID not available. Cannot load history.",
                isLoading = false,
                boughtItems = emptyList()
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getBoughtShoppingItems(currentFamId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        error = "Error loading shopping history: ${e.message}",
                        isLoading = false
                    )
                }
                .collect { items ->
                    _uiState.value = _uiState.value.copy(
                        boughtItems = items.sortedByDescending { it.boughtTimestamp ?: it.timestamp }, // Sort by bought time
                        isLoading = false
                    )
                }
        }
    }

    // Call this if family context can change while viewing history
    fun setFamilyIdAndReload(newFamilyId: String) {
        _uiState.value = _uiState.value.copy(currentFamilyId = newFamilyId, error = null)
        loadShoppingHistory()
    }
}
