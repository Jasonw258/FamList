package com.example.famlistapp.ui.screens.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.famlistapp.SupabaseClient
import com.example.famlistapp.data.model.Family
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class JoinFamilyUiState(
    val familyCode: String = "",
    val nickname: String = "",
    val avatar: String = "", // Placeholder for avatar data/path
    val joinedFamily: Family? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class JoinFamilyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(JoinFamilyUiState())
    val uiState: StateFlow<JoinFamilyUiState> = _uiState

    fun updateFamilyCode(code: String) {
        _uiState.value = _uiState.value.copy(familyCode = code)
    }

    fun updateNickname(nickname: String) {
        _uiState.value = _uiState.value.copy(nickname = nickname)
    }

    // TODO: Add function to update avatar

    fun joinFamily() {
        if (_uiState.value.familyCode.isBlank() || _uiState.value.nickname.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Family code and nickname cannot be empty.")
            return
        }
        if (_uiState.value.familyCode.length != 6) {
            _uiState.value = _uiState.value.copy(error = "Family code must be 6 digits.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val family = SupabaseClient.joinFamily(
                familyCode = _uiState.value.familyCode,
                userNickname = _uiState.value.nickname,
                userAvatar = _uiState.value.avatar // Placeholder
            )
            if (family != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    joinedFamily = family
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to join family. Invalid code or error occurred."
                )
            }
        }
    }
}
