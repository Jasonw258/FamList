package com.example.famlistapp.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// For animations:
// import androidx.compose.animation.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.famlistapp.ui.theme.FamListAppTheme
import kotlin.random.Random
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFamilyScreen(
    onFamilyCreated: () -> Unit
) {
    var familyName by remember { mutableStateOf(TextFieldValue("")) }
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    val familyCode = remember { (100000..999999).random().toString() } // Simulated 6-digit code
    val clipboardManager = LocalClipboardManager.current

    // Potential screen transition animation:
    // AnimatedVisibility(visible = true, enter = slideInHorizontally(), exit = slideOutHorizontally()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp) // Adjusted padding
                .imePadding(), // Handles keyboard overlap
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("创建你的家庭", style = MaterialTheme.typography.headlineMedium) // Create your family
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = familyName,
                onValueChange = { familyName = it },
                label = { Text("家庭名称") }, // Family Name
                modifier = Modifier.fillMaxWidth(),
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
            Spacer(modifier = Modifier.height(24.dp))

            Text("家庭邀请码:", style = MaterialTheme.typography.titleMedium) // Family Invite Code:
            Text(
                text = familyCode,
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 36.sp), // Larger font
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly // Better distribution
            ) {
                Button(
                    onClick = { /* TODO: WeChat Share - non-functional */ },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Share Icon", modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("微信分享") // WeChat Share
                }
                OutlinedButton( // Using OutlinedButton for visual distinction
                    onClick = { clipboardManager.setText(AnnotatedString(familyCode)) /* TODO: Show toast/feedback */ },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Copy Icon", modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("复制邀请码") // Copy Invite Code
                }
            }
            Spacer(modifier = Modifier.weight(1f)) // Pushes button to bottom

            Button(
                onClick = onFamilyCreated,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp) // Padding for bottom button
            ) {
                Text("创建家庭", style = MaterialTheme.typography.titleLarge) // Create Family
            }
        }
    // }
}

@Preview(showBackground = true)
@Composable
fun CreateFamilyScreenPreview() {
    FamListAppTheme {
        CreateFamilyScreen(onFamilyCreated = {})
    }
}
