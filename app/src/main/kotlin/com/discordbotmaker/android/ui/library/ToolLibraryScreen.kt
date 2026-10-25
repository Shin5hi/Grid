package com.discordbotmaker.android.ui.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.discordbotmaker.android.ui.theme.AppColors

// â”€â”€â”€ Tool Library â€” High-Density Server Tree View â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
//
// Discord-style collapsible tree: uppercase category headers with branch lines
// (drawn on a Canvas), kebab-case channel rows, a compact 30dp row height, and
// a featured "Orion Assistant" banner. No Monospace anywhere; it uses SansSerif.
//
// â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

enum class ChannelType { TEXT, VOICE, ANNOUNCEMENT, STAGE }

data class TreeChannel(val id: String, val name: String, val type: ChannelType, val isPro: Boolean = false, val label: String = "", val count: Int = 0, val icon: String = "")

data class TreeCategory(val id: String, val name: String, val accentColor: Color, val channels: List<TreeChannel> = emptyList(), val initiallyExpanded: Boolean = false, val count: Int = 0)

private val sansSerif = FontFamily.SansSerif

private fun iconForType(type: ChannelType): String = when (type) {
    ChannelType.TEXT -> "#"
    ChannelType.VOICE -> "ðŸ”Š"
    ChannelType.ANNOUNCEMENT -> "ðŸ“¢"
    ChannelType.STAGE -> "ðŸ“¡"
}

private val categories = listOf(
    TreeCategory(
        id = "inbox",
        name = "00. INBOX & CAPTURE",
        accentColor = AppColors.Primary,
        initiallyExpanded = true,
        count = 12,
        channels = listOf(
            TreeChannel("token-connect", "token-connect", ChannelType.TEXT, label = "Quick Capture", icon = "Q"),
            TreeChannel("grid-origin", "grid-origin", ChannelType.VOICE, label = "Inbox Zero", icon = "Z")
        )
    ),
    TreeCategory(
        id = "organize",
        name = "01. ORGANIZE",
        accentColor = AppColors.SecondaryBlue,
        count = 24,
        channels = listOf(
            TreeChannel("notes-hub", "notes-hub", ChannelType.TEXT, label = "Knowledge Management", count = 8, icon = "K"),
            TreeChannel("obsidian", "obsidian", ChannelType.TEXT, label = "Obsidian", icon = "O"),
            TreeChannel("notion", "notion", ChannelType.TEXT, label = "Notion", icon = "N"),
            TreeChannel("logseq", "logseq", ChannelType.TEXT, label = "Logseq", icon = "L"),
            TreeChannel("roam", "roam", ChannelType.TEXT, label = "Roam Research", icon = "R"),
            TreeChannel("anytype", "anytype", ChannelType.TEXT, label = "Anytype", icon = "A"),
            TreeChannel("auto-mod", "auto-mod", ChannelType.TEXT, label = "Task Management", count = 7, icon = "T"),
            TreeChannel("auto-ban", "auto-ban", ChannelType.TEXT, isPro = true, label = "File Management", count = 9, icon = "F")
        )
    ),
    TreeCategory(
        id = "create",
        name = "02. CREATE",
        accentColor = AppColors.AccentRocket,
        count = 32,
        channels = listOf(
            TreeChannel("chat-ai", "chat-ai", ChannelType.TEXT, label = "Content Creation", count = 12, icon = "C"),
            TreeChannel("summaries", "summaries", ChannelType.ANNOUNCEMENT, isPro = true, label = "Design & Visuals", count = 10, icon = "D"),
            TreeChannel("writing", "writing", ChannelType.TEXT, label = "Writing", count = 6, icon = "W"),
            TreeChannel("audio-notes", "audio-notes", ChannelType.VOICE, label = "Audio & Music", count = 4, icon = "M")
        )
    ),
    TreeCategory(
        id = "automate",
        name = "03. AUTOMATE",
        accentColor = AppColors.AccentBrain,
        count = 18,
        channels = listOf(
            TreeChannel("orion-assistant", "orion-assistant", ChannelType.STAGE, isPro = true, label = "Automation Tools", count = 7, icon = "A"),
            TreeChannel("music-player", "music-player", ChannelType.VOICE, label = "Integrations", count = 6, icon = "I"),
            TreeChannel("scripts", "scripts", ChannelType.TEXT, label = "Scripting", count = 5, icon = "S")
        )
    ),
    TreeCategory(
        id = "develop",
        name = "04. DEVELOP",
        accentColor = AppColors.AccentChart,
        count = 26,
        channels = listOf(
            TreeChannel("stats-dashboard", "stats-dashboard", ChannelType.TEXT, isPro = true, label = "Code Editors", count = 8, icon = "E"),
            TreeChannel("playlists", "playlists", ChannelType.VOICE, label = "Frameworks", count = 7, icon = "F"),
            TreeChannel("devtools", "devtools", ChannelType.TEXT, label = "Dev Tools", count = 11, icon = "T")
        )
    ),
    TreeCategory(
        id = "archive",
        name = "99. ARCHIVE",
        accentColor = AppColors.TextMuted,
        count = 4,
        channels = listOf(
            TreeChannel("settings", "settings", ChannelType.TEXT, label = "Archive", icon = "A")
        )
    )
)

/**
 * Displays the searchable, collapsible tool library.
 *
 * @param onToolSelected Called with the selected tool's display name.
 */
@Composable
fun ToolLibraryScreen(
    onToolSelected: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    val expanded = remember { mutableStateMapOf<String, Boolean>() }
    categories.forEach { cat ->
        if (cat.id !in expanded.keys) expanded[cat.id] = cat.initiallyExpanded
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {
        TreeHeader()

        SearchBar(query = query, onQueryChange = { query = it })

        Spacer(Modifier.height(8.dp))

        FeaturedOrionBanner(onClick = { onToolSelected("Orion Assistant") })

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            val filtered = filterCategories(categories, query)
            filtered.forEach { category ->
                TreeCategorySection(
                    category = category,
                    isExpanded = expanded[category.id] ?: false,
                    onToggle = { expanded[category.id] = !(expanded[category.id] ?: false) },
                    onChannelClick = onToolSelected
                )
            }
        }
    }
}

private fun filterCategories(
    all: List<TreeCategory>,
    query: String
): List<TreeCategory> {
    if (query.isBlank()) return all
    return all.mapNotNull { category ->
        val matched = category.channels.filter { it.name.contains(query, ignoreCase = true) }
        if (category.name.contains(query, ignoreCase = true) || matched.isNotEmpty()) {
            category.copy(channels = matched.ifEmpty { category.channels })
        } else {
            null
        }
    }
}

// â”€â”€â”€ Header with Grid brand mark â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

@Composable
private fun TreeHeader() {
    Surface(color = AppColors.Surface, tonalElevation = 0.dp, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(AppColors.Primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = sansSerif
                )
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = "Grid",
                    color = AppColors.TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = sansSerif
                )
                Text(
                    text = "Server Tree View",
                    color = AppColors.TextMuted,
                    fontSize = 11.sp,
                    fontFamily = sansSerif
                )
            }
        }
    }
}

// â”€â”€â”€ Search bar â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AppColors.InputBackground)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        if (query.isEmpty()) {
            Text(
                text = "Search tools",
                color = AppColors.TextMuted,
                fontSize = 14.sp,
                fontFamily = sansSerif
            )
        }
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = TextStyle(
                color = AppColors.TextPrimary,
                fontSize = 14.sp,
                fontFamily = sansSerif
            ),
            cursorBrush = SolidColor(AppColors.Primary),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// â”€â”€â”€ Featured "Asistente OriÃ³n" compact banner â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

@Composable
private fun FeaturedOrionBanner(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AppColors.SurfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "ðŸ“¡", fontSize = 16.sp)
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Orion Assistant",
            color = AppColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = sansSerif,
            modifier = Modifier.weight(1f)
        )
        Surface(color = AppColors.Primary, shape = RoundedCornerShape(4.dp)) {
            Text(
                text = "NEW",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = sansSerif,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(text = "\u203A", color = AppColors.TextMuted, fontSize = 16.sp)
    }
}

// --- Category section with collapsible channels ---

@Composable
private fun TreeCategorySection(
    category: TreeCategory,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onChannelClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .clickable { onToggle() }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(category.accentColor)
            )
            Spacer(Modifier.width(8.dp))
            CountChip(count = category.count)
            Spacer(Modifier.width(8.dp))
            Text(
                text = category.name,
                color = AppColors.TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
                fontFamily = sansSerif,
                modifier = Modifier.weight(1f)
            )
            val chevronRotation by animateFloatAsState(
                targetValue = if (isExpanded) 90f else 0f,
                label = "chevronRotation"
            )
            Text(
                text = "â€º",
                color = AppColors.TextMuted,
                fontSize = 16.sp,
                fontFamily = sansSerif,
                modifier = Modifier.rotate(chevronRotation)
            )
        }

        AnimatedVisibility(visible = isExpanded, enter = expandVertically()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                category.channels.forEachIndexed { index, channel ->
                    val isLast = index == category.channels.lastIndex
                    ChannelRow(
                        channel = channel,
                        isLast = isLast,
                        onClick = { onChannelClick(channel.id) }
                    )
                }
            }
        }
    }
}

// â”€â”€â”€ Channel row with a Canvas-drawn branch line â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

@Composable
private fun ChannelRow(
    channel: TreeChannel,
    isLast: Boolean,
    onClick: () -> Unit
) {
    val branchColor = AppColors.Divider
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .width(28.dp)
                .fillMaxHeight()
        ) {
            val stroke = 1.dp.toPx()
            val x = 8.dp.toPx()
            // Vertical line: full height, or half when it's the last channel
            val verticalEnd = if (isLast) size.height / 2f else size.height
            drawLine(
                color = branchColor,
                start = Offset(x, 0f),
                end = Offset(x, verticalEnd),
                strokeWidth = stroke
            )
            // Horizontal branch line connecting to the row label
            drawLine(
                color = branchColor,
                start = Offset(x, size.height / 2f),
                end = Offset(x + 12.dp.toPx(), size.height / 2f),
                strokeWidth = stroke
            )
        }

        CountChip(count = channel.count)
        Spacer(Modifier.width(6.dp))
        Text(
            text = channel.icon.ifEmpty { iconForType(channel.type) },
            color = AppColors.TextMuted,
            fontSize = 13.sp,
            fontFamily = sansSerif
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = channel.label.ifEmpty { channel.name },
            color = AppColors.TextSecondary,
            fontSize = 14.sp,
            fontFamily = sansSerif,
            modifier = Modifier.weight(1f)
        )
        if (channel.isPro) {
            Surface(
                color = AppColors.AccentBrain.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "PRO",
                    color = AppColors.AccentBrain,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = sansSerif,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
    }
}

@Composable
private fun CountChip(count: Int) {
    if (count <= 0) return
    Surface(color = AppColors.SurfaceVariant, shape = RoundedCornerShape(6.dp)) {
        Text(text = count.toString(), color = AppColors.TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium, fontFamily = sansSerif, modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp))
    }
}
