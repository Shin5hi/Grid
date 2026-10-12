package com.discordbotmaker.android.ui.dashboard

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.discordbotmaker.android.R
import com.discordbotmaker.android.ui.theme.AppColors

// ─── Bot Status Model ──────────────────────────────────────────────

data class BotStatus(
    val isOnline: Boolean = false,
    val botName: String = "MyDiscordBot",
    val serverCount: Int = 0,
    val memberCount: Int = 0,
    val uptimeFormatted: String = "—",
    val ping: Int = 0,
    val isLoggedIn: Boolean = false,
    val avatarUrl: String = "",
    val username: String = ""
)

/**
 * Real numbers for the summary tiles, from GET /api/status.
 * A null count means "not loaded yet"; the tile shows a dash.
 */
data class DashboardSummary(
    val backendOnline: Boolean,
    val commands: Int? = null,
    val deployments: Int? = null,
    val queueLength: Int? = null,
    val playbackState: String? = null
)

// ─── Module Card Descriptor ────────────────────────────────────────

private data class ModuleCard(
    val title: String,
    val icon: String,
    val description: String,
    val accentColor: Color
)

private val modules = listOf(
    ModuleCard(
        title = "Live Console",
        icon = "📟",
        description = "Real-time log stream with color-coded severity levels.",
        accentColor = AppColors.Primary
    ),
    ModuleCard(
        title = "AI AutoMod",
        icon = "🛡️",
        description = "Gemini-powered toxicity filter, spam & link protection.",
        accentColor = AppColors.AccentShield
    ),
    ModuleCard(
        title = "Command Builder",
        icon = "⚡",
        description = "Visual slash-command editor with embed & meme support.",
        accentColor = AppColors.AccentBolt
    ),
    ModuleCard(
        title = "Launch New Bot",
        icon = "🚀",
        description = "Connect token, configure & deploy a new bot instance.",
        accentColor = AppColors.AccentRocket
    ),
    ModuleCard(
        title = "Music Player",
        icon = "🎵",
        description = "Stream music to Discord voice channels with a queue.",
        accentColor = AppColors.AccentMusic
    ),
    ModuleCard(
        title = "Statistics",
        icon = "📊",
        description = "Server reach, members and activity over time.",
        accentColor = AppColors.AccentChart
    )
)

// ─── Main Dashboard Screen — High-Fidelity ──────────────────────────
//
// Updated to include:
// • "Login with Discord" button in the Discord OAuth2 style
// • Bot status card with refined metrics
// • Module grid (2 columns) with flat cards (8dp corners, 0 elevation)
// • Bottom navigation bar is handled in AppNavigation scaffold
//
// ───────────────────────────────────────────────────────────────────────────

@Composable
fun MainDashboardScreen(
    botStatus: BotStatus = BotStatus(),
    greetingName: String = "Shin5hi",
    summary: DashboardSummary? = null,
    onNavigateToConsole: () -> Unit = {},
    onNavigateToAutoMod: () -> Unit = {},
    onNavigateToCommandBuilder: () -> Unit = {},
    onNavigateToBotCreation: () -> Unit = {},
    onNavigateToMusic: () -> Unit = {},
    onNavigateToStats: () -> Unit = {},
    onLoginWithDiscord: () -> Unit = {}
) {
    // Order must match the `modules` list above.
    val callbacks = listOf(
        onNavigateToConsole,
        onNavigateToAutoMod,
        onNavigateToCommandBuilder,
        onNavigateToBotCreation,
        onNavigateToMusic,
        onNavigateToStats
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .verticalScroll(rememberScrollState())
    ) {
        DashboardHeader(botStatus)

        Spacer(Modifier.height(20.dp))

        GreetingBlock(name = greetingName)

        Spacer(Modifier.height(20.dp))

        SummaryGrid(summary)

        Spacer(Modifier.height(20.dp))

        // Show Login or Bot Status based on auth state
        if (!botStatus.isLoggedIn) {
            LoginWithDiscordCard(onLoginWithDiscord = onLoginWithDiscord)
        } else {
            BotStatusCard(botStatus)
        }

        Spacer(Modifier.height(20.dp))

        // Section heading
        Text(
            text = "ACCESO RÁPIDO",
            color = AppColors.TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(10.dp))

        ModuleList(modules, callbacks)

        Spacer(Modifier.height(24.dp))

        // Quick actions row
        QuickActionsRow()

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Grid by Shin5hi · v2.0",
            color = AppColors.TextMuted,
            fontSize = 10.sp,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp) // space for bottom nav
        )
    }
}

// ─── Header ───────────────────────────────────────────────────────────

@Composable
private fun GreetingBlock(name: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text(
            text = buildAnnotatedString {
                append("Hola, ")
                withStyle(SpanStyle(color = AppColors.Primary)) { append(name) }
            },
            color = AppColors.TextPrimary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "¿Qué vas a crear hoy?",
            color = AppColors.TextSecondary,
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif
        )
    }
}


@Composable
private fun DashboardHeader(botStatus: BotStatus) {
    Surface(
        color = AppColors.Surface,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Grid brand mark
                Image(
                    painter = painterResource(R.drawable.ic_grid_logo),
                    contentDescription = "Grid",
                    modifier = Modifier.size(30.dp)
                )

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Grid",
                        color = AppColors.TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                    if (botStatus.isLoggedIn && botStatus.username.isNotEmpty()) {
                        Text(
                            text = botStatus.username,
                            color = AppColors.TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }

            // Notification bell
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(AppColors.SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔔",
                    fontSize = 16.sp
                )
            }
        }
    }
}

// ─── Login with Discord Card ─────────────────────────────────────────

@Composable
private fun LoginWithDiscordCard(
    onLoginWithDiscord: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Discord logo placeholder
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(AppColors.Primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎮",
                    fontSize = 28.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Connect Your Bot",
                color = AppColors.TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Sign in with Discord to manage your bots,\nview servers, and deploy modules.",
                color = AppColors.TextSecondary,
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onLoginWithDiscord,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Primary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "🔍",
                    fontSize = 18.sp
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Login with Discord",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "or connect with bot token →",
                color = AppColors.TextLink,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

// ─── Bot Status Card ─────────────────────────────────────────────

@Composable
private fun BotStatusCard(status: BotStatus) {
    val statusColor by animateColorAsState(
        targetValue = if (status.isOnline) AppColors.Success else AppColors.Error,
        animationSpec = tween(durationMillis = 500),
        label = "statusColor"
    )

    val pulseTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by pulseTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AppColors.Primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🤖", fontSize = 22.sp)
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = status.botName,
                        color = AppColors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        text = if (status.isOnline) "Uptime: ${status.uptimeFormatted}" else "Last seen: N/A",
                        color = AppColors.TextMuted,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }

                Surface(
                    color = statusColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    statusColor.copy(
                                        alpha = if (status.isOnline) pulseAlpha else 1f
                                    )
                                )
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = if (status.isOnline) "Online" else "Offline",
                            color = statusColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = AppColors.Divider, thickness = 1.dp)
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(label = "Servers", value = "${status.serverCount}", color = AppColors.Primary)
                StatItem(label = "Members", value = "${status.memberCount}", color = AppColors.Primary)
                StatItem(label = "Ping", value = "${status.ping}ms", color = AppColors.Warning)
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = color,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )
        Text(
            text = label,
            color = AppColors.TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif
        )
    }
}

// ─── Quick Actions Row ───────────────────────────────────────────

@Composable
private fun QuickActionsRow() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "QUICK ACTIONS",
            color = AppColors.TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionChip("🔄", "Restart Bot", Modifier.weight(1f))
            QuickActionChip("📊", "View Logs", Modifier.weight(1f))
            QuickActionChip("⬥️", "Update", Modifier.weight(1f))
        }
    }
}

@Composable
private fun QuickActionChip(icon: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        color = AppColors.SurfaceVariant,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                text = label,
                color = AppColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1
            )
        }
    }
}

// ─── 2×2 Module Grid ─────────────────────────────────────────────

@Composable
private fun ModuleList(cards: List<ModuleCard>, callbacks: List<() -> Unit>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        cards.forEachIndexed { index, card ->
            ModuleRow(card = card, onClick = callbacks.getOrElse(index) { {} })
        }
    }
}

@Composable
private fun ModuleRow(card: ModuleCard, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.Surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(card.accentColor.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = card.icon, fontSize = 18.sp)
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = card.title,
                color = AppColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = card.description,
                color = AppColors.TextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.SansSerif
            )
        }

        Text(text = "›", color = AppColors.TextMuted, fontSize = 22.sp)
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: String,
    val activeIcon: String
)

val bottomNavItems = listOf(
    BottomNavItem(route = "dashboard", label = "Inicio", icon = "🏠", activeIcon = "🏠"),
    BottomNavItem(route = "tool_library", label = "The Tree", icon = "🧩", activeIcon = "🧩"),
    BottomNavItem(route = "settings", label = "Ajustes", icon = "⚙️", activeIcon = "⚙️")
)

@Composable
fun GridBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Surface(
        color = AppColors.NavBarBackground,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val isActive = currentRoute == item.route
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onNavigate(item.route) }
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    if (isActive) {
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(AppColors.Primary)
                        )
                        Spacer(Modifier.height(4.dp))
                    }

                    Text(
                        text = if (isActive) item.activeIcon else item.icon,
                        fontSize = 20.sp
                    )

                    Spacer(Modifier.height(2.dp))

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

// ─── Summary tiles (2 x 2) ─────────────────────────────────────────

@Composable
private fun SummaryGrid(summary: DashboardSummary?) {
    val dash = "—"
    val playback = when (summary?.playbackState) {
        "playing" -> "reproduciendo"
        "paused" -> "en pausa"
        "stopped" -> "detenido"
        else -> "sin datos"
    }
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            SummaryTile(
                label = "Comandos",
                value = summary?.commands?.toString() ?: dash,
                hint = "guardados en el backend",
                accent = AppColors.AccentBolt,
                modifier = Modifier.weight(1f)
            )
            SummaryTile(
                label = "Despliegues",
                value = summary?.deployments?.toString() ?: dash,
                hint = "bots lanzados",
                accent = AppColors.AccentRocket,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            SummaryTile(
                label = "Cola de música",
                value = summary?.queueLength?.toString() ?: dash,
                hint = playback,
                accent = AppColors.AccentMusic,
                modifier = Modifier.weight(1f)
            )
            SummaryTile(
                label = "Backend",
                value = when (summary) {
                    null -> dash
                    else -> if (summary.backendOnline) "En línea" else "Sin conexión"
                },
                hint = if (summary?.backendOnline == true) "Redis conectado" else "revisa la URL en Ajustes",
                accent = if (summary?.backendOnline == true) AppColors.Success else AppColors.Error,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SummaryTile(
    label: String,
    value: String,
    hint: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = label,
                color = accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                color = AppColors.TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = hint,
                color = AppColors.TextMuted,
                fontSize = 11.sp,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
