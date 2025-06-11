package com.example.famlistapp.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource // Import stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.famlistapp.R // Import R class for string resources
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
// For animations, you might use:
// import androidx.compose.animation.AnimatedVisibility
// import androidx.compose.animation.ExperimentalAnimationApi
// import androidx.compose.animation.fadeIn
// import androidx.compose.animation.fadeOut
import com.example.famlistapp.ui.theme.FamListAppTheme

@Composable
fun OnboardingScreen(
    onCreateFamilyClicked: () -> Unit,
    onJoinFamilyClicked: () -> Unit
) {
    // Potential screen transition animation:
    // AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp), // Increased horizontal padding
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.welcome_to_famlist), // Use stringResource
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Button(
                onClick = onCreateFamilyClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.create_new_family), style = MaterialTheme.typography.titleMedium) // Use stringResource
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onJoinFamilyClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary) // Example of different color
            ) {
                Text(stringResource(R.string.join_existing_family), style = MaterialTheme.typography.titleMedium) // Use stringResource
            }
            // Add other elements like app logo or a brief description if needed
        }
    // }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    FamListAppTheme {
        OnboardingScreen(
            onCreateFamilyClicked = {},
            onJoinFamilyClicked = {}
        )
    }
}
