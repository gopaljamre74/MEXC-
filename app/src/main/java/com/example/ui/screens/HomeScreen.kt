package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CryptoTicker
import com.example.ui.ExchangeUiState
import com.example.ui.NavigationTab
import com.example.ui.components.CryptoTickerRow
import com.example.ui.components.LatencyGaugeBadge
import com.example.ui.theme.MexcAccentGold
import com.example.ui.theme.MexcBorder
import com.example.ui.theme.MexcColdBlue
import com.example.ui.theme.MexcGreen
import com.example.ui.theme.MexcPrimary
import com.example.ui.theme.MexcRed
import com.example.ui.theme.MexcSurface
import com.example.ui.theme.MexcSurfaceVariant
import com.example.ui.theme.MexcTextPrimary
import com.example.ui.theme.MexcTextSecondary

@Composable
fun HomeScreen(
    uiState: ExchangeUiState,
    onNavigateTab: (NavigationTab) -> Unit,
    onSelectTicker: (CryptoTicker) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen")
    ) {
        // 1. Top Bar Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onNavigateTab(NavigationTab.Security) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MexcSurfaceVariant)
                            .border(1.dp, MexcPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_mexc_logo_1786372190672),
                            contentDescription = "User Profile",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "MEXC VIP",
                                color = MexcTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "KYC Level 2",
                                tint = MexcPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = if (uiState.securitySettings?.isTwoFactorEnabled == true) "2FA Secure" else "Set 2FA",
                            color = if (uiState.securitySettings?.isTwoFactorEnabled == true) MexcGreen else MexcAccentGold,
                            fontSize = 10.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    LatencyGaugeBadge(latencyMs = uiState.lastExecutionLatencyMs ?: 12)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { onNavigateTab(NavigationTab.Markets) },
                        modifier = Modifier.testTag("home_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Market",
                            tint = MexcTextPrimary
                        )
                    }
                }
            }
        }

        // 2. Hero Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MexcSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .testTag("home_hero_banner")
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_mexc_hero_banner_1786372251741),
                        contentDescription = "MEXC Zero Fee Spot & Kickstarters Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        color = Color.Black.copy(alpha = 0.55f),
                        modifier = Modifier.matchParentSize()
                    ) {}
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        Surface(
                            color = MexcPrimary,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "ZERO MAKER FEES",
                                color = Color.Black,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "MEXC Kickstarter & Spot Launch",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Trade BTC, ETH & MX with <20ms ultra low latency",
                            color = MexcTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // 3. Announcement Ticker
        item {
            Surface(
                color = MexcSurfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = MexcAccentGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MEXC Air-Gapped Cold Storage Protection System upgraded. 0% spot fee active.",
                        color = MexcTextPrimary,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }

        // 4. Quick Action Grid
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickShortcutButton(
                    icon = Icons.Default.AccountBalanceWallet,
                    label = "Deposit",
                    color = MexcGreen,
                    onClick = { onNavigateTab(NavigationTab.Wallet) }
                )
                QuickShortcutButton(
                    icon = Icons.Default.RocketLaunch,
                    label = "Launchpad",
                    color = MexcColdBlue,
                    onClick = { onNavigateTab(NavigationTab.Markets) }
                )
                QuickShortcutButton(
                    icon = Icons.Default.FlashOn,
                    label = "100x Futures",
                    color = Color(0xFFFF5252),
                    onClick = { onNavigateTab(NavigationTab.Futures) }
                )
                QuickShortcutButton(
                    icon = Icons.Default.Lock,
                    label = "Cold Vault",
                    color = MexcAccentGold,
                    onClick = { onNavigateTab(NavigationTab.Wallet) }
                )
                QuickShortcutButton(
                    icon = Icons.Default.Security,
                    label = "2FA Center",
                    color = MexcPrimary,
                    onClick = { onNavigateTab(NavigationTab.Security) }
                )
            }
        }

        // 5. Hot Market Trend Cards Row
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Market Highlights",
                    color = MexcTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(modifier = Modifier.width(8.dp)) }
                    items(uiState.tickers.filter { it.isHot }.take(4)) { ticker ->
                        HighlightCard(
                            ticker = ticker,
                            onClick = {
                                onSelectTicker(ticker)
                                onNavigateTab(NavigationTab.Trade)
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.width(8.dp)) }
                }
            }
        }

        // 6. Main Tickers List Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hot Crypto Pairs",
                    color = MexcTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "See All >",
                    color = MexcPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateTab(NavigationTab.Markets) }
                )
            }
        }

        // 7. Ticker List Rows
        items(uiState.tickers) { ticker ->
            CryptoTickerRow(
                ticker = ticker,
                onClick = {
                    onSelectTicker(ticker)
                    onNavigateTab(NavigationTab.Trade)
                }
            )
        }
    }
}

@Composable
fun QuickShortcutButton(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("shortcut_$label")
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f))
                .border(1.dp, color.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = MexcTextPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun HighlightCard(
    ticker: CryptoTicker,
    onClick: () -> Unit
) {
    val isPositive = ticker.change24h >= 0
    val changeColor = if (isPositive) MexcGreen else MexcRed

    Card(
        colors = CardDefaults.cardColors(containerColor = MexcSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
        modifier = Modifier
            .width(135.dp)
            .clickable(onClick = onClick)
            .testTag("highlight_card_${ticker.baseAsset}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = ticker.symbol,
                color = MexcTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$${if (ticker.price < 1.0) String.format("%.6f", ticker.price) else String.format("%.2f", ticker.price)}",
                color = MexcTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${if (isPositive) "+" else ""}${String.format("%.2f", ticker.change24h)}%",
                color = changeColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
