package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.ExchangeViewModel
import com.example.ui.NavigationTab
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MarketsScreen
import com.example.ui.screens.SecurityScreen
import com.example.ui.screens.TradeScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.MexcPrimary
import com.example.ui.theme.MexcSurface
import com.example.ui.theme.MexcTextSecondary
import com.example.ui.theme.MexcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MexcTheme {
                ExchangeApp()
            }
        }
    }
}

@Composable
fun ExchangeApp(viewModel: ExchangeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val navItems = listOf(
        Triple(NavigationTab.Home, "Home", Icons.Default.Home),
        Triple(NavigationTab.Markets, "Markets", Icons.Default.TrendingUp),
        Triple(NavigationTab.Trade, "Trade", Icons.Default.SwapHoriz),
        Triple(NavigationTab.Wallet, "Wallet", Icons.Default.AccountBalanceWallet),
        Triple(NavigationTab.Security, "Security", Icons.Default.Security)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MexcSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                navItems.forEach { (tab, title, icon) ->
                    val selected = uiState.selectedTab == tab
                    NavigationBarItem(
                        selected = selected,
                        onClick = { viewModel.selectTab(tab) },
                        icon = { Icon(imageVector = icon, contentDescription = title) },
                        label = { Text(title, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MexcPrimary,
                            selectedTextColor = MexcPrimary,
                            unselectedIconColor = MexcTextSecondary,
                            unselectedTextColor = MexcTextSecondary,
                            indicatorColor = MexcPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.route}")
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (uiState.selectedTab) {
            NavigationTab.Home -> HomeScreen(
                uiState = uiState,
                onNavigateTab = { tab -> viewModel.selectTab(tab) },
                onSelectTicker = { ticker ->
                    viewModel.updateSelectedTicker(ticker)
                    viewModel.selectTab(NavigationTab.Trade)
                },
                modifier = modifier
            )
            NavigationTab.Markets -> MarketsScreen(
                uiState = uiState,
                onSearchQueryChange = { q -> viewModel.setSearchQuery(q) },
                onCategoryFilterSelect = { cat -> viewModel.setMarketCategoryFilter(cat) },
                onSelectTicker = { ticker ->
                    viewModel.updateSelectedTicker(ticker)
                    viewModel.selectTab(NavigationTab.Trade)
                },
                onNavigateTab = { tab -> viewModel.selectTab(tab) },
                modifier = modifier
            )
            NavigationTab.Trade, NavigationTab.Futures -> TradeScreen(
                uiState = uiState,
                onTradeTypeChange = { type -> viewModel.setTradeType(type) },
                onOrderModeChange = { mode -> viewModel.setOrderMode(mode) },
                onPriceInputChange = { price -> viewModel.setTradePriceInput(price) },
                onAmountInputChange = { amt -> viewModel.setTradeAmountInput(amt) },
                onExecuteTrade = { viewModel.executeTrade() },
                modifier = modifier
            )
            NavigationTab.Wallet -> WalletScreen(
                uiState = uiState,
                onOpenColdStorageModal = { assetSymbol -> viewModel.openColdStorageModal(assetSymbol) },
                onNavigateTab = { tab -> viewModel.selectTab(tab) },
                modifier = modifier
            )
            NavigationTab.Security -> SecurityScreen(
                uiState = uiState,
                onToggle2FA = { viewModel.toggleTwoFactorAuth() },
                onToggleBiometrics = { viewModel.toggleBiometrics() },
                onOpenColdStorageModal = { viewModel.openColdStorageModal() },
                onCloseColdStorageModal = { viewModel.closeColdStorageModal() },
                onTransferAmountChange = { amt -> viewModel.setTransferAmountInput(amt) },
                onUser2FACodeChange = { code -> viewModel.setUser2FACode(code) },
                onSubmitColdStorageTransfer = { viewModel.submitColdStorageTransfer() },
                modifier = modifier
            )
        }
    }
}
