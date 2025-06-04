package com.example.famlistapp.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
// For animations:
// import androidx.compose.animation.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.famlistapp.ui.theme.FamListAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinFamilyScreen(
    onJoinFamilyClicked: (familyCode: String, nickname: String) -> Unit
) {
    var familyCode by remember { mutableStateOf(TextFieldValue("")) }
    var nickname by remember { mutableStateOf(TextFieldValue("")) }

    // Potential screen transition animation:
    // AnimatedVisibility(visible = true, enter = slideInHorizontally(), exit = slideOutHorizontally()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp) // Adjusted padding
                .imePadding(), // Handles keyboard overlap
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Align elements to top
        ) {
            Text("加入一个家庭", style = MaterialTheme.typography.headlineMedium) // Join a family
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = familyCode,
                onValueChange = {
                    // Limit family code input to 6 digits
                    if (it.text.length <= 6) {
                        familyCode = it
                    }
                },
                label = { Text("6位家庭码") }, // 6-digit Family Code
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword), // Use NumberPassword for digits
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("我的昵称") }, // My Nickname
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Placeholder for Avatar Selection - more visually distinct
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable { /* TODO: Implement avatar selection */ }
            ) {
                Text("选择头像", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSecondaryContainer) // Select Avatar
            }
            Spacer(modifier = Modifier.weight(1f)) // Pushes button to bottom

            Button(
                onClick = { onJoinFamilyClicked(familyCode.text, nickname.text) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), // Padding for bottom button
                enabled = familyCode.text.length == 6 && nickname.text.isNotBlank() // Enable button only when valid
            ) {
                Text("加入家庭", style = MaterialTheme.typography.titleLarge) // Join Family
            }
        }
    // }
}

@Preview(showBackground = true)
@Composable
fun JoinFamilyScreenPreview() {
    FamListAppTheme {
        JoinFamilyScreen(onJoinFamilyClicked = { _, _ -> })
    }
}
