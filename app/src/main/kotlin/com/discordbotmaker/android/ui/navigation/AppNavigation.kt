package com.discordbotmaker.android.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.discordbotmaker.android.data.BackendServices
import com.discordbotmaker.android.data.MusicQueueDto
import com.discordbotmaker.android.data.MusicTrackDto
import com.discordbotmaker.android.ui.automod.AutoModConfig
import com.discordbotmaker.android.ui.automod.AutoModScreen
import com.discordbotmaker.android.ui.commands.BotCommand
import com.discordbotmaker.android.ui.commands.CommandBuilderScreen
import com.discordbotmaker.android.ui.components.OrionBubble
import com.discordbotmaker.android.ui.console.LiveConsoleScreen
import com.discordbotmaker.android.ui.console.LiveConsoleViewModel
import com.discordbotmaker.android.ui.console.logLevelFromBackend
import com.discordbotmaker.android.ui.dashboard.BotStatus
import com.discordbotmaker.android.ui.dashboard.DashboardSummary
import com.discordbotmaker.android.ui.dashboard.GridBottomNavBar
import com.discordbotmaker.android.ui.dashboard.MainDashboardScreen
import com.discordbotmaker.android.ui.doubt.DoubtAssistantScreen
import com.discordbotmaker.android.ui.launch.BotCreationScreen
import com.discordbotmaker.android.ui.library.ToolLibraryScreen
import com.discordbotmaker.android.ui.music.MusicPlayerScreen
import com.discordbotmaker.android.ui.music.MusicPlayerState
import com.discordbotmaker.android.ui.music.PlaybackState
import com.discordbotmaker.android.ui.music.Track
import com.discordbotmaker.android.ui.settings.SettingsScreen
import com.discordbotmaker.android.ui.splash.OriginLoadingScreen
import com.discordbotmaker.android.ui.splash.SplashScreen
import com.discordbotmaker.android.ui.stats.StatsDashboardScreen
import com.discordbotmaker.android.ui.theme.AppColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object AppRoutes {
    const val SPLASH               = "splash"
    const val GRID_ORIGIN_LOADING  = "grid_origin_loading"
    const val DASHBOARD            = "dashboard"
    const val TOOL_LIBRARY         = "tool_library"
    const val SETTINGS             = "settings"
    const val LIVE_CONSOLE         = "live_console"
    const val AUTO_MOD             = "auto_mod"
    const val COMMAND_BUILDER      = "command_builder"
    const val BOT_CREATION         = "bot_creation"
    const val DOUBT_ASSISTANT      = "doubt_assistant"
    const val MUSIC_PLAYER         = "music_player"
    const val STATS                = "stats"
}

private val bottomNavRoutes = setOf(
    AppRoutes.DASHBOARD,
    AppRoutes.TOOL_LIBRARY,
    AppRoutes.SETTINGS
)

// Screens where the Orion FAB should NOT appear
private val hideOrionRoutes = setOf(
    AppRoutes.SPLASH,
    AppRoutes.GRID_ORIGIN_LOADING,
    AppRoutes.DOUBT_ASSISTANT
)

/** How often the music queue is refreshed while the player is open. */
private const val MUSIC_POLL_MS = 4_000L

/** How often the dashboard summary tiles are refreshed. */
private const val DASHBOARD_POLL_MS = 15_000L

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    consoleViewModel: LiveConsoleViewModel = remember { LiveConsoleViewModel() },
    botStatus: BotStatus = BotStatus()
) {
    val context = LocalContext.current
    val services = remember(context) { BackendServices(context.applicationContext) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: AppRoutes.SPLASH
    val showBottomNav = currentRoute in bottomNavRoutes
    val showOrionBubble = currentRoute !in hideOrionRoutes

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                GridBottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(AppRoutes.DASHBOARD) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        },
        containerColor = AppColors.Background
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = AppRoutes.SPLASH,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(AppRoutes.SPLASH) {
                    SplashScreen(
                        onSplashComplete = {
                            navController.navigate(AppRoutes.DASHBOARD) {
                                popUpTo(AppRoutes.SPLASH) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                // Grid Origin Loading --- the branded animated loading screen
                // Accessible from the launch flow; navigates to Dashboard on completion.
                composable(AppRoutes.GRID_ORIGIN_LOADING) {
                    OriginLoadingScreen(
                        onLoadingComplete = {
                            navController.navigate(AppRoutes.DASHBOARD) {
                                popUpTo(AppRoutes.GRID_ORIGIN_LOADING) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(AppRoutes.DASHBOARD) {
                    // Summary tiles: real counts from the backend, refreshed while the dashboard is visible.
                    var summary by remember { mutableStateOf<DashboardSummary?>(null) }
                    LaunchedEffect(Unit) {
                        while (true) {
                            services.client.status()
                                .onSuccess { status ->
                                    summary = DashboardSummary(
                                        backendOnline = status.redis == "connected",
                                        commands = status.commands,
                                        deployments = status.deployments,
                                        queueLength = status.musicQueueLength,
                                        playbackState = status.musicPlaybackState
                                    )
                                }
                                .onFailure { summary = DashboardSummary(backendOnline = false) }
                            delay(DASHBOARD_POLL_MS)
                        }
                    }

                    MainDashboardScreen(
                        botStatus = botStatus,
                        summary = summary,
                        onNavigateToConsole = {
                            navController.navigate(AppRoutes.LIVE_CONSOLE) { launchSingleTop = true }
                        },
                        onNavigateToAutoMod = {
                            navController.navigate(AppRoutes.AUTO_MOD) { launchSingleTop = true }
                        },
                        onNavigateToCommandBuilder = {
                            navController.navigate(AppRoutes.COMMAND_BUILDER) { launchSingleTop = true }
                        },
                        onNavigateToBotCreation = {
                            navController.navigate(AppRoutes.BOT_CREATION) { launchSingleTop = true }
                        },
                        onNavigateToMusic = {
                            navController.navigate(AppRoutes.MUSIC_PLAYER) { launchSingleTop = true }
                        },
                        onNavigateToStats = {
                            navController.navigate(AppRoutes.STATS) { launchSingleTop = true }
                        },
                        onLoginWithDiscord = {}
                    )
                }

                composable(AppRoutes.TOOL_LIBRARY) {
                    ToolLibraryScreen(
                        onToolSelected = { toolName ->
                            when (toolName) {
                                "Asistente Orion" -> navController.navigate(AppRoutes.DOUBT_ASSISTANT) {
                                    launchSingleTop = true
                                }
                                else -> { }
                            }
                        }
                    )
                }

                composable(AppRoutes.SETTINGS) {
                    SettingsScreen(
                        initialUrl = services.config.baseUrl,
                        onSave = { services.config.baseUrl = it },
                        onTestConnection = { url -> services.clientFor(url).health() }
                    )
                }

                composable(AppRoutes.LIVE_CONSOLE) {
                    // Connected only while this screen is visible; the socket closes on exit.
                    LaunchedEffect(Unit) {
                        services.logStream.stream(
                            onStatus = consoleViewModel::setConnected,
                            onRecord = { record ->
                                consoleViewModel.appendRemote(
                                    level = logLevelFromBackend(record.level),
                                    source = record.source,
                                    message = record.message,
                                    timestampSeconds = record.timestampSeconds
                                )
                            }
                        )
                    }
                    LiveConsoleScreen(viewModel = consoleViewModel)
                }

                composable(AppRoutes.AUTO_MOD) {
                    val scope = rememberCoroutineScope()
                    var loaded by remember { mutableStateOf<AutoModConfig?>(null) }
                    var error by remember { mutableStateOf<String?>(null) }
                    var reloadKey by remember { mutableIntStateOf(0) }

                    LaunchedEffect(reloadKey) {
                        services.client.getAutoModConfig()
                            .onSuccess { loaded = it; error = null }
                            .onFailure { error = it.message ?: "Unknown error" }
                    }

                    val config = loaded
                    if (config == null) {
                        BackendStatusScreen(message = error, onRetry = { reloadKey++ })
                    } else {
                        AutoModScreen(
                            config = config,
                            onConfigChanged = { updated ->
                                scope.launch { services.client.putAutoModConfig(updated) }
                            }
                        )
                    }
                }

                composable(AppRoutes.COMMAND_BUILDER) {
                    val scope = rememberCoroutineScope()
                    var loaded by remember { mutableStateOf<List<BotCommand>?>(null) }
                    var error by remember { mutableStateOf<String?>(null) }
                    var reloadKey by remember { mutableIntStateOf(0) }

                    LaunchedEffect(reloadKey) {
                        services.client.listCommands()
                            .onSuccess { loaded = it; error = null }
                            .onFailure { error = it.message ?: "Unknown error" }
                    }

                    val commands = loaded
                    if (commands == null) {
                        BackendStatusScreen(message = error, onRetry = { reloadKey++ })
                    } else {
                        CommandBuilderScreen(
                            initialCommands = commands,
                            onSaveCommand = { command ->
                                scope.launch { services.client.createCommand(command) }
                            },
                            onDeleteCommand = { command ->
                                scope.launch { services.client.deleteCommand(command.name) }
                            },
                            onSaveToCloud = { list, done ->
                                scope.launch {
                                    val outcome = services.client.saveCommands(list)
                                    done(outcome.fold(onSuccess = { null }, onFailure = { it.message ?: "Save failed" }))
                                }
                            }
                        )
                    }
                }

                composable(AppRoutes.BOT_CREATION) {
                    val scope = rememberCoroutineScope()
                    BotCreationScreen(
                        onDeploy = { token, botName, onResult ->
                            scope.launch {
                                val outcome = services.client.deployBot(token, botName)
                                val error: String? = outcome.fold(
                                    onSuccess = { null },
                                    onFailure = { it.message ?: "Deploy failed" }
                                )
                                onResult(error)
                            }
                        }
                    )
                }

                composable(AppRoutes.MUSIC_PLAYER) {
                    val scope = rememberCoroutineScope()
                    var musicState by remember { mutableStateOf(MusicPlayerState()) }
                    var loaded by remember { mutableStateOf(false) }
                    var error by remember { mutableStateOf<String?>(null) }

                    val refresh: suspend () -> Unit = {
                        services.client.musicQueue()
                            .onSuccess {
                                musicState = it.toPlayerState()
                                loaded = true
                                error = null
                            }
                            .onFailure { error = it.message ?: "Unknown error" }
                    }

                    LaunchedEffect(Unit) {
                        while (true) {
                            refresh()
                            delay(MUSIC_POLL_MS)
                        }
                    }

                    if (!loaded) {
                        BackendStatusScreen(message = error, onRetry = { scope.launch { refresh() } })
                    } else {
                        MusicPlayerScreen(
                            state = musicState,
                            onPlay = {
                                // Resume when paused, otherwise start the queue.
                                val action = if (musicState.playbackState == PlaybackState.PAUSED) "resume" else "play"
                                scope.launch { services.client.musicCommand(action); refresh() }
                            },
                            onPause = { scope.launch { services.client.musicCommand("pause"); refresh() } },
                            onSkip = { scope.launch { services.client.musicCommand("skip"); refresh() } },
                            onStop = { scope.launch { services.client.musicCommand("stop"); refresh() } },
                            onAddTrack = { query ->
                                scope.launch { services.client.musicAdd(query); refresh() }
                            }
                        )
                    }
                }

                composable(AppRoutes.STATS) {
                    StatsDashboardScreen()
                }

                composable(AppRoutes.DOUBT_ASSISTANT) {
                    DoubtAssistantScreen(onAsk = { question -> services.client.askAssistant(question) })
                }
            }

            // -- Orion Floating Bubble --- Global overlay --
            if (showOrionBubble) {
                OrionBubble(
                    onClick = {
                        navController.navigate(AppRoutes.DOUBT_ASSISTANT) {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp)
                )
            }
        }
    }
}

/** Full-screen state for a screen whose data is still loading or failed to load. */
@Composable
private fun BackendStatusScreen(message: String?, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (message == null) {
                CircularProgressIndicator(color = AppColors.Primary)
            } else {
                Text(
                    text = "Could not reach the backend",
                    color = AppColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = message,
                    color = AppColors.TextSecondary,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Check the URL in Settings.",
                    color = AppColors.TextMuted,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = onRetry) {
                    Text("Retry", color = AppColors.Primary, fontFamily = FontFamily.SansSerif)
                }
            }
        }
    }
}

private fun MusicQueueDto.toPlayerState(): MusicPlayerState {
    val state = when (playbackState.lowercase()) {
        "playing" -> PlaybackState.PLAYING
        "paused" -> PlaybackState.PAUSED
        else -> PlaybackState.STOPPED
    }
    return MusicPlayerState(
        playbackState = state,
        currentTrack = nowPlaying?.toTrack(),
        progressSeconds = 0,
        queue = queue.map { it.toTrack() }
    )
}

private fun MusicTrackDto.toTrack() = Track(
    id = url.ifEmpty { title },
    title = title,
    artist = artist,
    durationSeconds = durationSeconds,
    url = url
)
