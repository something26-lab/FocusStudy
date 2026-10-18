package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.CheatsheetScreen
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.PlannerScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.TimerScreen
import com.example.ui.theme.FocusStudyTheme
import com.example.viewmodel.StudyViewModel

import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.IconButton
import com.example.ui.components.AuthAndIntroDialog
import com.example.ui.theme.ThemeMode
import com.example.ui.components.glass.LiquidGlassAtmosphere
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke

import com.example.ads.AdManager
import com.example.ads.BannerAdView

enum class StudyTab(val title: String, val icon: ImageVector) {
    TIMER("Đồng Hồ", Icons.Default.Timer),
    FLASHCARDS("Thẻ Nhớ", Icons.Default.Style),
    PLANNER("Kế Hoạch", Icons.Default.Checklist),
    STATS("Thống Kê", Icons.Default.BarChart),
    CHEATSHEET("Sổ Tay", Icons.Default.Description)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            android.webkit.WebView.enableSlowWholeDocumentDraw()
        } catch (_: Exception) {}
        AdManager.initialize(this)
        enableEdgeToEdge()
        setContent {
            val viewModel: StudyViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsState()
            FocusStudyTheme(themeMode = themeMode) {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: StudyViewModel) {
    var selectedTab by remember { mutableStateOf(StudyTab.TIMER) }
    val timerState by viewModel.timerState.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val userAccount by viewModel.userAccount.collectAsState()

    var showTopBarAuthDialog by remember { mutableStateOf(false) }
    var topBarDialogInitialTab by remember { mutableStateOf(1) }

    val completedToday = timerState.completedSessionsToday

    if (showTopBarAuthDialog) {
        AuthAndIntroDialog(
            userAccount = userAccount,
            initialTab = topBarDialogInitialTab,
            onDismiss = { showTopBarAuthDialog = false },
            onSignInWithGoogle = { name, email ->
                viewModel.signInWithGoogle(name, email)
                showTopBarAuthDialog = false
            },
            onCreateAccount = { name, email, goal ->
                viewModel.createCustomAccount(name, email, goal)
                showTopBarAuthDialog = false
            },
            onSignOut = {
                viewModel.signOut()
            },
            onUpdateDailyGoal = { goal ->
                viewModel.updateDailyGoal(goal)
            }
        )
    }

    val isGlass = themeMode == ThemeMode.LIQUID_GLASS

    LiquidGlassAtmosphere(
        modifier = Modifier.fillMaxSize(),
        isLiquidGlassActive = isGlass
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWideScreen = maxWidth >= 600.dp

            Scaffold(
                containerColor = if (isGlass) Color.Transparent else MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets.safeDrawing,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_study_logo),
                                    contentDescription = "FocusStudy Logo",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "FocusStudy",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        actions = {
                            // User Profile / Google Sign-In Button
                            IconButton(
                                onClick = {
                                    topBarDialogInitialTab = 1
                                    showTopBarAuthDialog = true
                                },
                                modifier = Modifier.testTag("top_bar_account_btn")
                            ) {
                                if (userAccount.isGoogleUser) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_google_logo),
                                        contentDescription = "Tài khoản Google",
                                        modifier = Modifier.size(22.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Tài khoản người dùng",
                                        tint = if (userAccount.isLoggedIn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Theme Toggle Button (Liquid Glass / Night / Day / System)
                            IconButton(
                                onClick = { viewModel.cycleThemeMode() },
                                modifier = Modifier.testTag("theme_mode_toggle_btn")
                            ) {
                                val (themeIcon, themeDesc) = when (themeMode) {
                                    ThemeMode.LIQUID_GLASS -> Pair(Icons.Default.WaterDrop, "Chế Độ Kính Lỏng (Liquid Glass)")
                                    ThemeMode.DARK -> Pair(Icons.Default.DarkMode, "Chế Độ Đêm (Bảo Vệ Mắt)")
                                    ThemeMode.LIGHT -> Pair(Icons.Default.LightMode, "Chế Độ Ngày (Tập Trung)")
                                    ThemeMode.SYSTEM -> Pair(Icons.Default.BrightnessAuto, "Tự Động Theo Hệ Thống")
                                }
                                Icon(
                                    imageVector = themeIcon,
                                    contentDescription = themeDesc,
                                    tint = when (themeMode) {
                                        ThemeMode.LIQUID_GLASS -> Color(0xFF38BDF8)
                                        ThemeMode.DARK -> MaterialTheme.colorScheme.secondary
                                        else -> MaterialTheme.colorScheme.primary
                                    }
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isGlass) Color(0x3538BDF8) else MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .then(
                                        if (isGlass) Modifier.border(
                                            1.dp,
                                            Brush.linearGradient(
                                                listOf(Color(0x80FFFFFF), Color(0x3038BDF8))
                                            ),
                                            RoundedCornerShape(12.dp)
                                        ) else Modifier
                                    )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = "Chuỗi ngày",
                                        tint = Color(0xFFF97316),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "$completedToday phiên",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isGlass) Color(0xFFBAE6FD) else MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = if (isGlass) Color(0x35141E33) else MaterialTheme.colorScheme.surface
                        )
                    )
                },
                bottomBar = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        BannerAdView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                        if (!isWideScreen) {
                            NavigationBar(
                                containerColor = if (isGlass) Color(0x60141E33) else MaterialTheme.colorScheme.surface,
                                tonalElevation = if (isGlass) 0.dp else 6.dp,
                                modifier = Modifier
                                    .testTag("bottom_navigation_bar")
                                    .then(
                                        if (isGlass) Modifier.border(
                                            BorderStroke(
                                                1.dp,
                                                Brush.verticalGradient(
                                                    listOf(Color(0x55FFFFFF), Color(0x1038BDF8))
                                                )
                                            )
                                        ) else Modifier
                                    )
                            ) {
                                StudyTab.values().forEach { tab ->
                                    val isSelected = selectedTab == tab
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { selectedTab = tab },
                                        icon = {
                                            Icon(
                                                imageVector = tab.icon,
                                                contentDescription = tab.title
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = tab.title,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = if (isGlass) Color(0xFF38BDF8) else MaterialTheme.colorScheme.primary,
                                            selectedTextColor = if (isGlass) Color(0xFF38BDF8) else MaterialTheme.colorScheme.primary,
                                            indicatorColor = if (isGlass) Color(0x3538BDF8) else MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        modifier = Modifier.testTag("nav_tab_${tab.name}")
                                    )
                                }
                            }
                        }
                    }
                }
            ) { innerPadding ->
                if (isWideScreen) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavigationRail(
                            containerColor = if (isGlass) Color(0x45141E33) else MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxHeight().testTag("side_navigation_rail")
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            StudyTab.values().forEach { tab ->
                                val isSelected = selectedTab == tab
                                NavigationRailItem(
                                    selected = isSelected,
                                    onClick = { selectedTab = tab },
                                    icon = {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.title
                                        )
                                    },
                                    label = { Text(tab.title) },
                                    modifier = Modifier.testTag("rail_tab_${tab.name}")
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        // Content Pane
                        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            TabContent(selectedTab = selectedTab, viewModel = viewModel)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TabContent(selectedTab = selectedTab, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun TabContent(
    selectedTab: StudyTab,
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    when (selectedTab) {
        StudyTab.TIMER -> TimerScreen(viewModel = viewModel, modifier = modifier)
        StudyTab.FLASHCARDS -> FlashcardsScreen(viewModel = viewModel, modifier = modifier)
        StudyTab.PLANNER -> PlannerScreen(viewModel = viewModel, modifier = modifier)
        StudyTab.STATS -> StatsScreen(viewModel = viewModel, modifier = modifier)
        StudyTab.CHEATSHEET -> CheatsheetScreen(viewModel = viewModel, modifier = modifier)
    }
}
