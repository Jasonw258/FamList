package com.example.famlistapp.ui.screens.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.famlistapp.SupabaseClient
import com.example.famlistapp.data.model.Family
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CreateFamilyUiState(
    val familyName: String = "",
    val nickname: String = "",
    val avatar: String = "", // Placeholder for avatar data/path
    val createdFamily: Family? = null,
    val familyCode: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class CreateFamilyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateFamilyUiState())
    val uiState: StateFlow<CreateFamilyUiState> = _uiState

    fun updateFamilyName(name: String) {
        _uiState.value = _uiState.value.copy(familyName = name)
    }

    fun updateNickname(nickname: String) {
        _uiState.value = _uiState.value.copy(nickname = nickname)
    }

    // TODO: Add function to update avatar

    fun createFamily() {
        if (_uiState.value.familyName.isBlank() || _uiState.value.nickname.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Family name and nickname cannot be empty.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = SupabaseClient.createFamily(
                familyName = _uiState.value.familyName,
                userNickname = _uiState.value.nickname,
                userAvatar = _uiState.value.avatar // Placeholder
            )
            if (result.first != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    createdFamily = result.first,
                    familyCode = result.second
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.second ?: "Failed to create family."
                )
            }
        }
    }
}
