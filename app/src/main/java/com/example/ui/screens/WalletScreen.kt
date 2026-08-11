package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Outbox
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ExchangeUiState
import com.example.ui.NavigationTab
import com.example.ui.theme.MexcBorder
import com.example.ui.theme.MexcColdBlue
import com.example.ui.theme.MexcGreen
import com.example.ui.theme.MexcPrimary
import com.example.ui.theme.MexcSurface
import com.example.ui.theme.MexcTextPrimary
import com.example.ui.theme.MexcTextSecondary

@Composable
fun WalletScreen(
    uiState: ExchangeUiState,
    onOpenColdStorageModal: (String) -> Unit,
    onNavigateTab: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalEquity = uiState.walletAssets.sumOf { asset ->
        val ticker = uiState.tickers.find { it.baseAsset == asset.assetSymbol }
        val price = ticker?.price ?: if (asset.assetSymbol == "USDT") 1.0 else 100.0
        (asset.hotBalance + asset.coldStorageBalance) * price
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("wallet_screen")
    ) {
        // Top Equity Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MexcPrimary),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("wallet_equity_card")
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Total Wallet Equity (USDT)",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "$${String.format("%.2f", totalEquity)}",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "≈ $${String.format("%.2f", totalEquity * 0.9998)} USD",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onNavigateTab(NavigationTab.Trade) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("wallet_deposit_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null, tint = MexcPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Deposit", color = MexcPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        OutlinedButton(
                            onClick = { onOpenColdStorageModal("BTC") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("wallet_withdraw_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Outbox, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Cold Vault", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Vault Status Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MexcSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, tint = MexcGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Vault Protection: Active", color = MexcTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Hot & Cold multi-sig separation", color = MexcTextSecondary, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = { onNavigateTab(NavigationTab.Security) },
                        colors = ButtonDefaults.buttonColors(containerColor = MexcSurface),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder)
                    ) {
                        Text("Security", color = MexcPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Asset List Header
        item {
            Text(
                text = "Asset Holdings",
                color = MexcTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (uiState.walletAssets.isEmpty()) {
            item {
                Text("No assets found in wallet.", color = MexcTextSecondary, fontSize = 12.sp)
            }
        } else {
            items(uiState.walletAssets) { asset ->
                val ticker = uiState.tickers.find { it.baseAsset == asset.assetSymbol }
                val price = ticker?.price ?: if (asset.assetSymbol == "USDT") 1.0 else 100.0
                val assetTotalUsdt = (asset.hotBalance + asset.coldStorageBalance) * price

                Card(
                    colors = CardDefaults.cardColors(containerColor = MexcSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .testTag("wallet_asset_item_${asset.assetSymbol}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(MexcPrimary.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = asset.assetSymbol.take(3),
                                    color = MexcPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(asset.assetSymbol, color = MexcTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Hot: ${asset.hotBalance}", color = MexcTextSecondary, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Cold: ${asset.coldStorageBalance}", color = MexcColdBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("$${String.format("%.2f", assetTotalUsdt)}", color = MexcTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Total: ${asset.hotBalance + asset.coldStorageBalance} ${asset.assetSymbol}", color = MexcTextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
