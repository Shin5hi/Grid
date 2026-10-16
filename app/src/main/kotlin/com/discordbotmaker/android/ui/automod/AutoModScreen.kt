package com.discordbotmaker.android.ui.automod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

import com.discordbotmaker.android.ui.theme.AppColors

// â”€â”€â”€ AutoMod Configuration State â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”

data class AutoModConfig(
    val toxicityFilterEnabled: Boolean = false,
    val toxicitySensitivity: Float = 0.7f,       // 0.0 â€“ 1.0
    val toxicityAction: ToxicityAction = ToxicityAction.DELETE,
    val linkBlockingEnabled: Boolean = false,
    val allowWhitelistedLinks: Boolean = true,
    val spamProtectionEnabled: Boolean = false,
    val spamMessageThreshold: Int = 5,            // messages per window
    val spamWindowSeconds: Int = 10,
    val spamMuteDurationMinutes: Int = 5
)

enum class ToxicityAction(val label: String, val icon: String) {
    WARN("Warn", "âš "),
    DELETE("Delete", "ðŸ—‘"),
    MUTE("Mute", "ðŸ”‡"),
    BAN("Ban", "ðŸš«")
}

// â”€â”€â”€ AutoMod Screen â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”

@Composable
fun AutoModScreen(
    config: AutoModConfig = AutoModConfig(),
    onConfigChanged: (AutoModConfig) -> Unit = {}
) {
    var state by remember { mutableStateOf(config) }

    // Propagate changes upward
    fun update(block: AutoModConfig.() -> AutoModConfig) {
        state = state.block()
        onConfigChanged(state)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .verticalScroll(rememberScrollState())
    ) {
        // â”€â”€ Header â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”
        ScreenHeader()

        Spacer(Modifier.height(16.dp))

        // â”€â”€ 1. AI Toxicity Filter (Gemini) â”â”â”â”â”â”â”â”â”â”â”â”â”â”
        ModCard(
            title = "AI Toxicity Filter",
            subtitle = "Powered by Google Gemini",
            icon = "ðŸ§ ",
            accentColor = AppColors.Primary,
            enabled = state.toxicityFilterEnabled,
            onToggle = { update { copy(toxicityFilterEnabled = it) } }
        ) {
            AnimatedVisibility(
                visible = state.toxicityFilterEnabled,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Sensitivity slider
                    LabeledSlider(
                        label = "Sensitivity",
                        value = state.toxicitySensitivity,
                        valueLabel = "${(state.toxicitySensitivity * 100).roundToInt()}%",
                        accentColor = AppColors.Primary,
                        onValueChange = { update { copy(toxicitySensitivity = it) } }
                    )

                    Spacer(Modifier.height(16.dp))

                    // Action selector
                    Text(
                        text = "ACTION ON VIOLATION",
                        color = AppColors.TextSecondary,
                        fontSize = 10.sp,
                        
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ToxicityAction.entries.forEach { action ->
                            ActionChip(
                                action = action,
                                selected = state.toxicityAction == action,
                                accentColor = AppColors.Primary,
                                onClick = { update { copy(toxicityAction = action) } }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // â”€â”€ 2. Link Blocking â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”
        ModCard(
            title = "Link Blocking",
            subtitle = "Block unauthorized URLs in messages",
            icon = "ðŸ”—",
            accentColor = AppColors.Warning,
            enabled = state.linkBlockingEnabled,
            onToggle = { update { copy(linkBlockingEnabled = it) } }
        ) {
            AnimatedVisibility(
                visible = state.linkBlockingEnabled,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    NeonSwitchRow(
                        label = "Allow whitelisted domains",
                        checked = state.allowWhitelistedLinks,
                        accentColor = AppColors.Warning,
                        onCheckedChange = { update { copy(allowWhitelistedLinks = it) } }
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Whitelisted: discord.com, youtube.com, github.com",
                        color = AppColors.TextMuted,
                        fontSize = 11.sp,
                        
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // â”€â”€ 3. Spam Protection â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”
        ModCard(
            title = "Spam Protection",
            subtitle = "Rate limiting & auto-mute for spam",
            icon = "ðŸ›¡ï¸",
            accentColor = AppColors.Info,
            enabled = state.spamProtectionEnabled,
            onToggle = { update { copy(spamProtectionEnabled = it) } }
        ) {
            AnimatedVisibility(
                visible = state.spamProtectionEnabled,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Message threshold slider
                    LabeledSlider(
                        label = "Message Threshold",
                        value = state.spamMessageThreshold.toFloat() / 20f,
                        valueLabel = "${state.spamMessageThreshold} msgs",
                        accentColor = AppColors.Info,
                        onValueChange = {
                            update { copy(spamMessageThreshold = (it * 20).roundToInt().coerceIn(2, 20)) }
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    // Time window slider
                    LabeledSlider(
                        label = "Time Window",
                        value = state.spamWindowSeconds.toFloat() / 60f,
                        valueLabel = "${state.spamWindowSeconds}s",
                        accentColor = AppColors.Info,
                        onValueChange = {
                            update { copy(spamWindowSeconds = (it * 60).roundToInt().coerceIn(5, 60)) }
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    // Mute duration slider
                    LabeledSlider(
                        label = "Mute Duration",
                        value = state.spamMuteDurationMinutes.toFloat() / 60f,
                        valueLabel = "${state.spamMuteDurationMinutes} min",
                        accentColor = AppColors.Info,
                        onValueChange = {
                            update { copy(spamMuteDurationMinutes = (it * 60).roundToInt().coerceIn(1, 60)) }
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // â”€â”€ Save Button â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”
        Button(
            onClick = { onConfigChanged(state) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.Success.copy(alpha = 0.15f),
                contentColor = AppColors.Success
            )
        ) {
            Text(
                text = "â–¸ DEPLOY AUTOMOD CONFIG",
                
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(Modifier.height(32.dp))
    }
}

// â”€â”€â”€ Screen Header â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”

@Composable
private fun ScreenHeader() {
    Surface(
        color = AppColors.Surface,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text(
                text = "â–Œ AUTOMOD",
                color = AppColors.Success,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Configure AI-powered moderation rules for your server.",
                color = AppColors.TextSecondary,
                fontSize = 12.sp,
                
            )
        }
    }
}

// â”€â”€â”€ Reusable Mod Card â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”

@Composable
private fun ModCard(
    title: String,
    subtitle: String,
    icon: String,
    accentColor: Color,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
    expandedContent: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (enabled) accentColor.copy(alpha = 0.4f) else AppColors.SurfaceBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                if (enabled) {
                    Brush.verticalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.05f),
                            AppColors.Surface
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(AppColors.Surface, AppColors.Surface)
                    )
                }
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = icon, fontSize = 22.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            color = if (enabled) accentColor else AppColors.TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            
                        )
                        Text(
                            text = subtitle,
                            color = AppColors.TextMuted,
                            fontSize = 11.sp,
                            
                        )
                    }
                }

                Switch(
                    checked = enabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = accentColor,
                        checkedTrackColor = accentColor.copy(alpha = 0.3f),
                        uncheckedThumbColor = AppColors.TextSecondary,
                        uncheckedTrackColor = AppColors.SwitchTrackOff
                    )
                )
            }

            expandedContent()
        }
    }
}

// â”€â”€â”€ Labeled Slider â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”

@Composable
private fun LabeledSlider(
    label: String,
    value: Float,
    valueLabel: String,
    accentColor: Color,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label.uppercase(),
                color = AppColors.TextSecondary,
                fontSize = 10.sp,
                
                letterSpacing = 1.sp
            )
            Text(
                text = valueLabel,
                color = accentColor,
                fontSize = 12.sp,
                
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(4.dp))
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = AppColors.SliderInactive
            )
        )
    }
}

// â”€â”€â”€ Neon Switch Row â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”

@Composable
private fun NeonSwitchRow(
    label: String,
    checked: Boolean,
    accentColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = AppColors.TextPrimary,
            fontSize = 13.sp,
            
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = accentColor,
                checkedTrackColor = accentColor.copy(alpha = 0.3f),
                uncheckedThumbColor = AppColors.TextSecondary,
                uncheckedTrackColor = AppColors.SwitchTrackOff
            )
        )
    }
}

// â”€â”€â”€ Action Chip â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”â”

@Composable
private fun RowScope.ActionChip(
    action: ToxicityAction,
    selected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) accentColor.copy(alpha = 0.15f) else Color.Transparent,
            contentColor = if (selected) accentColor else AppColors.TextSecondary
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = Brush.linearGradient(
                colors = if (selected) listOf(accentColor, accentColor)
                else listOf(AppColors.SurfaceBorder, AppColors.SurfaceBorder)
            )
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Text(
            text = "${action.icon}\n${action.label}",
            fontSize = 10.sp,
            
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            lineHeight = 16.sp
        )
    }
}

