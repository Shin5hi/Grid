package com.discordbotmaker.android.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.discordbotmaker.android.data.BackendConfig
import com.discordbotmaker.android.ui.theme.AppColors
import kotlinx.coroutines.launch

/**
 * Lets the user point the app at a backend and check that it answers.
 *
 * [onSave] persists the URL. [onTestConnection] receives the typed value, not the saved one,
 * so the user can test before committing.
 */
@Composable
fun SettingsScreen(
    initialUrl: String,
    onSave: (String) -> Unit = {},
    onTestConnection: suspend (String) -> Result<Unit> = { Result.success(Unit) }
) {
    var url by remember { mutableStateOf(initialUrl) }
    var status by remember { mutableStateOf<String?>(null) }
    var statusIsError by remember { mutableStateOf(false) }
    var testing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val valid = BackendConfig.isValidUrl(url)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            color = AppColors.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Backend connection",
            color = AppColors.TextSecondary,
            fontSize = 13.sp,
            fontFamily = FontFamily.SansSerif
        )

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = url,
            onValueChange = {
                url = it
                status = null
            },
            label = { Text("Backend URL") },
            singleLine = true,
            isError = url.isNotBlank() && !valid,
            supportingText = {
                Text(
                    text = if (url.isBlank() || valid) {
                        "Emulator default: ${BackendConfig.DEFAULT_BASE_URL}"
                    } else {
                        "Must start with http:// or https://"
                    },
                    fontFamily = FontFamily.SansSerif
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    onSave(BackendConfig.normalize(url))
                    status = "Saved"
                    statusIsError = false
                },
                enabled = valid,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
            ) {
                Text("Save", fontFamily = FontFamily.SansSerif)
            }

            OutlinedButton(
                onClick = {
                    testing = true
                    status = null
                    scope.launch {
                        val result = onTestConnection(BackendConfig.normalize(url))
                        testing = false
                        statusIsError = result.isFailure
                        status = result.fold(
                            onSuccess = { "Connected: backend is healthy" },
                            onFailure = { "Could not connect: ${it.message ?: "unknown error"}" }
                        )
                    }
                },
                enabled = valid && !testing
            ) {
                Text(
                    text = if (testing) "Testing…" else "Test connection",
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        status?.let { message ->
            Spacer(Modifier.height(16.dp))
            Text(
                text = message,
                color = if (statusIsError) AppColors.Error else AppColors.Success,
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}
