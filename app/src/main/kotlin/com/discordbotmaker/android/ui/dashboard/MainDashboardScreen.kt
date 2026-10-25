package com.discordbotmaker.android.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.discordbotmaker.android.ui.theme.AppColors

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimatedVisibilityApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.viewModelScope
import kotlinx.coroutines.launch

// The dashboard is scoped to the bot supplied by its caller.
/**
 * Status and metrics for the bot currently selected in the app.
 *
 * @property botName Display name of the selected bot, or empty when none is selected.
 * @property serverCount Number of servers where the bot is present.
 * @property memberCount Total members across those servers.
 * @property uptimeFormatted Human-readable runtime duration.
 * @property ping Latest latency in milliseconds.
 */
data class BotStatus(
    val isOnline: Boolean = false,
    val botName: String = "",
    val serverCount: Int = 0,
    val memberCount: Int = 0,
    val uptimeFormatted: String = "-",
    val ping: Int = 0,
    val isLoggedIn: Boolean = false,
    val avatarUrl: String = "",
    val username: String = ""
)

/**
 * Renders the dashboard for the current bot state.
 *
 * @param botStatus Bot details displayed by the dashboard.
 * @param onNavigateToConsole Opens the live-console screen.
 * @param onNavigateToAutoMod Opens the AutoMod screen.
 * @param onNavigateToCommandBuilder Opens the command editor.
 * @param onNavigateToBotCreation Opens the bot launch flow.
 * @param onLoginWithDiscord Starts the caller-provided Discord login flow.
 * @param viewModel Supplies loading, error, or dashboard content state.
 */
@Composable
fun MainDashboardScreen(
    botStatus: BotStatus = BotStatus(),
    onNavigateToConsole: () -> Unit = {},
    onNavigateToAutoMod: () -> Unit = {},
    onNavigateToCommandBuilder: () -> Unit = {},
    onNavigateToBotCreation: () -> Unit = {},
    onLoginWithDiscord: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is DashboardUiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize().background(AppColors.Background)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(38.dp).clip(RoundedCornerShape(11.dp))
                            .background(AppColors.Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "G", color = AppColors.TextPrimary, fontSize = 20.sp,
                            fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(text = "Grid", color = AppColors.TextSecondary, fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.SansSerif)
                }
                Spacer(Modifier.height(40.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    Text(text = "Bot overview", color = AppColors.TextPrimary, fontSize = 32.sp,
                        fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
                }
                Spacer(Modifier.height(40.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = AppColors.Primary,
                        strokeWidth = 4.dp
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Loading bot overview...",
                        color = AppColors.TextSecondary,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }

        is DashboardUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize().background(AppColors.Background)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(38.dp).clip(RoundedCornerShape(11.dp))
                            .background(AppColors.Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "G", color = AppColors.TextPrimary, fontSize = 20.sp,
                            fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(text = "Grid", color = AppColors.TextSecondary, fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.SansSerif)
                }
                Spacer(Modifier.height(40.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "\u26A0",
                        color = AppColors.Error,
                        fontSize = 40.sp
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = uiState.message,
                            color = AppColors.TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Check your connection and try again.",
                            color = AppColors.TextSecondary,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = uiState.onRetry,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.Primary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(text = "Retry", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        is DashboardUiState.Content -> {
            Column(
                modifier = Modifier.fillMaxSize().background(AppColors.Background)
                    .verticalScroll(rememberScrollState()).padding(bottom = 84.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        enter = fadeIn(animationSpec = tween(300)) +
                            slideInVertically(initialOffsetY = { 20f }, animationSpec = tween(350)),
                        modifier = Modifier.size(38.dp).clip(RoundedCornerShape(11.dp))
                            .background(AppColors.Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "G", color = AppColors.TextPrimary, fontSize = 20.sp,
                            fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
                    }
                    Spacer(Modifier.width(10.dp))
                    AnimatedVisibility(
                        enter = fadeIn(animationSpec = tween(350)) +
                            slideInVertically(initialOffsetY = { 20f }, animationSpec = tween(400)),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(text = "Grid", color = AppColors.TextSecondary, fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.SansSerif)
                    }
                }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "BOT OVERVIEW",
                    color = AppColors.TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = botStatus.botName.ifBlank { "No bot selected" },
                    color = AppColors.TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (botStatus.isOnline) "Online" else "Offline",
                    color = if (botStatus.isOnline) AppColors.Success else AppColors.TextMuted,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        Spacer(Modifier.height(18.dp))
        Text(
            text = if (botStatus.botName.isBlank()) {
                "Create or select a bot to view its dashboard."
            } else {
                "Status and metrics for this bot."
            },
            color = AppColors.TextSecondary, fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
        )
        Spacer(Modifier.height(18.dp))
        Text(
            text = "SMART SUMMARY",
            color = AppColors.TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(Modifier.weight(1f)) {
                AnimatedStatCard(title = "Servers", value = botStatus.serverCount.toString(), icon = "#",
                    iconColor = AppColors.Primary, delayMs = 0)
            }
            Box(Modifier.weight(1f)) {
                AnimatedStatCard(title = "Members", value = botStatus.memberCount.toString(), icon = "M",
                    iconColor = AppColors.Primary, delayMs = 80)
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(Modifier.weight(1f)) {
                AnimatedStatCard(title = "Uptime", value = botStatus.uptimeFormatted, icon = "U",
                    iconColor = AppColors.SecondaryBlue, delayMs = 160)
            }
            Box(Modifier.weight(1f)) {
                AnimatedStatCard(title = "Ping", value = if (botStatus.ping > 0) "${botStatus.ping} ms" else "—", icon = "P",
                    iconColor = AppColors.SecondaryBlue, delayMs = 240)
            }
        }
        Spacer(Modifier.height(22.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "QUICK ACCESS", color = AppColors.TextMuted, fontSize = 11.sp,
                fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
            Text(text = "View all", color = AppColors.Primary, fontSize = 13.sp,
                fontWeight = FontWeight.Medium, fontFamily = FontFamily.SansSerif)
        }
        Spacer(Modifier.height(10.dp))
        AnimatedQuickItem(title = "Live Console", subtitle = "View bot events and logs",
            icon = "C", onClick = onNavigateToConsole, delayMs = 0)
        AnimatedQuickItem(title = "AutoMod", subtitle = "Review moderation settings",
            icon = "A", onClick = onNavigateToAutoMod, delayMs = 80)
        AnimatedQuickItem(title = "Commands", subtitle = "Manage bot commands",
            icon = "/", onClick = onNavigateToCommandBuilder, delayMs = 160)
        AnimatedQuickItem(title = "Bot Launch", subtitle = "Create or connect a bot",
            icon = "B", onClick = onNavigateToBotCreation, delayMs = 240)
            }
        }
    }
}

@Composable
private fun AnimatedStatCard(
    title: String,
    value: String,
    icon: String,
    iconColor: androidx.compose.ui.graphics.Color,
    delayMs: Long
) {
    AnimatedVisibility(
        enter = fadeIn(
            animationSpec = tween(durationMillis = 400, delayMillis = delayMs)
        ) + slideInVertically(
            initialOffsetY = { 30f },
            animationSpec = tween(durationMillis = 450, delayMillis = delayMs)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        GridStatCard(title = title, value = value, icon = icon, iconColor = iconColor)
    }
}

@Composable
private fun AnimatedQuickItem(
    title: String,
    subtitle: String,
    icon: String,
    onClick: () -> Unit,
    delayMs: Long
) {
    AnimatedVisibility(
        enter = fadeIn(
            animationSpec = tween(durationMillis = 400, delayMillis = delayMs)
        ) + slideInVertically(
            initialOffsetY = { 30f },
            animationSpec = tween(durationMillis = 450, delayMillis = delayMs)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        GridQuickItem(title = title, subtitle = subtitle, icon = icon, onClick = onClick)
    }
}

@Composable
private fun GridStatCard(
    title: String,
    value: String,
    icon: String,
    iconColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = AppColors.TextSecondary, fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = value, color = AppColors.TextPrimary, fontSize = 30.sp,
                    fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.weight(1f))
                Text(text = icon, color = iconColor, fontSize = 18.sp)
            }
        }
    }
}

@Composable
private fun GridQuickItem(
    title: String,
    subtitle: String,
    icon: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp))
                    .background(AppColors.SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, color = AppColors.TextPrimary, fontSize = 18.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = AppColors.TextPrimary, fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.SansSerif)
                Text(text = subtitle, color = AppColors.TextSecondary, fontSize = 12.sp,
                    fontFamily = FontFamily.SansSerif)
            }
            Text(text = ">", color = AppColors.TextMuted, fontSize = 20.sp)
        }
    }
}

data class BottomNavItem(val route: String, val label: String, val icon: String)

val bottomNavItems = listOf(
    BottomNavItem(route = "dashboard", label = "Home", icon = "H"),
    BottomNavItem(route = "bot_creation", label = "Projects", icon = "P"),
    BottomNavItem(route = "tool_library", label = "The Tree", icon = "T"),
    BottomNavItem(route = "doubt_assistant", label = "Notes", icon = "N"),
    BottomNavItem(route = "settings", label = "Settings", icon = "A")
)

/**
 * Renders the dashboard's bottom navigation items.
 *
 * @param currentRoute Route currently selected in the navigation graph.
 * @param onNavigate Called with the selected destination route.
 */
@Composable
fun GridBottomNavBar(currentRoute: String, onNavigate: (String) -> Unit) {
    Surface(
        color = AppColors.NavBarBackground,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val isActive = currentRoute == item.route
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigate(item.route) }
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.icon,
                        fontSize = 18.sp,
                        color = if (isActive) AppColors.NavItemActive else AppColors.NavItemInactive
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = item.label,
                        color = if (isActive) AppColors.NavItemActive else AppColors.NavItemInactive,
                        fontSize = 10.sp,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }
    }
}

// ═════════════════════════════════════════════════════════════════════════════════
// Dashboard view state transitions: loading, content, and recoverable error.
// ═════════════════════════════════════════════════════════════════════════════════

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Content(val isLoggedIn: Boolean = false) : DashboardUiState()
    data class Error(val message: String, val onRetry: () -> Unit) : DashboardUiState()
}

/**
 * Holds dashboard loading, content, and retry state.
 *
 * The current implementation simulates loading and is not connected to a data
 * source; bot details are supplied separately to [MainDashboardScreen].
 */
class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState.Loading
    )

    init {
        viewModelScope.launch {
            // Simulate loading bot data from the backend.
            kotlinx.coroutines.delay(800)
            _uiState.value = DashboardUiState.Content(isLoggedIn = false)
        }
    }

    /** Replays the local loading sequence and returns to the content state. */
    fun retry() {
        _uiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _uiState.value = DashboardUiState.Content(isLoggedIn = false)
        }
    }
}
