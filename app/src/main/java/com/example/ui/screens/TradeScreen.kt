package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ExchangeUiState
import com.example.ui.components.CandlestickChart
import com.example.ui.components.LatencyGaugeBadge
import com.example.ui.components.OrderBookWidget
import com.example.ui.theme.MexcBorder
import com.example.ui.theme.MexcGreen
import com.example.ui.theme.MexcPrimary
import com.example.ui.theme.MexcRed
import com.example.ui.theme.MexcSurface
import com.example.ui.theme.MexcSurfaceVariant
import com.example.ui.theme.MexcTextPrimary
import com.example.ui.theme.MexcTextSecondary

@Composable
fun TradeScreen(
    uiState: ExchangeUiState,
    onTradeTypeChange: (String) -> Unit,
    onOrderModeChange: (String) -> Unit,
    onPriceInputChange: (String) -> Unit,
    onAmountInputChange: (String) -> Unit,
    onExecuteTrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = uiState.selectedTicker
    val isBuy = uiState.activeTradeType == "BUY"
    val actionColor = if (isBuy) MexcGreen else MexcRed

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
            .testTag("trade_screen")
    ) {
        // Top Pair Info Bar
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MexcSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = selected.symbol,
                                color = MexcTextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = if (selected.change24h >= 0) MexcGreen else MexcRed,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${if (selected.change24h >= 0) "+" else ""}${selected.change24h}%",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "24h High: $${selected.high24h} | 24h Low: $${selected.low24h}",
                            color = MexcTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    LatencyGaugeBadge(latencyMs = uiState.lastExecutionLatencyMs ?: 12)
                }
            }
        }

        // K-Line Candlestick Chart
        item {
            CandlestickChart(candles = uiState.candlesticks)
        }

        // Trading Execution Desk: Orderbook on left, Order Form on right
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Order Book Side
                Column(modifier = Modifier.weight(0.45f)) {
                    Text(
                        text = "Order Book",
                        color = MexcTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OrderBookWidget(orderBookData = uiState.orderBook)
                }

                // Order Placement Form Side
                Column(
                    modifier = Modifier
                        .weight(0.55f)
                        .background(MexcSurface, RoundedCornerShape(12.dp))
                        .border(1.dp, MexcBorder, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    // Buy / Sell Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = if (isBuy) MexcGreen else MexcSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onTradeTypeChange("BUY") }
                                .testTag("trade_buy_tab")
                        ) {
                            Text(
                                text = "Buy",
                                color = if (isBuy) Color.White else MexcTextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }

                        Surface(
                            color = if (!isBuy) MexcRed else MexcSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onTradeTypeChange("SELL") }
                                .testTag("trade_sell_tab")
                        ) {
                            Text(
                                text = "Sell",
                                color = if (!isBuy) Color.White else MexcTextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Order Mode (Limit / Market)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Limit Order",
                            color = if (uiState.activeOrderMode == "LIMIT") MexcPrimary else MexcTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { onOrderModeChange("LIMIT") }
                                .padding(2.dp)
                        )
                        Text(
                            text = "Market Order",
                            color = if (uiState.activeOrderMode == "MARKET") MexcPrimary else MexcTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { onOrderModeChange("MARKET") }
                                .padding(2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Price Input
                    OutlinedTextField(
                        value = uiState.tradePriceInput,
                        onValueChange = onPriceInputChange,
                        label = { Text("Price (USDT)", fontSize = 10.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MexcSurfaceVariant,
                            unfocusedContainerColor = MexcSurfaceVariant,
                            focusedBorderColor = MexcPrimary,
                            unfocusedBorderColor = MexcBorder,
                            focusedTextColor = MexcTextPrimary,
                            unfocusedTextColor = MexcTextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trade_price_input")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Amount Input
                    OutlinedTextField(
                        value = uiState.tradeAmountInput,
                        onValueChange = onAmountInputChange,
                        label = { Text("Amount (${selected.baseAsset})", fontSize = 10.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MexcSurfaceVariant,
                            unfocusedContainerColor = MexcSurfaceVariant,
                            focusedBorderColor = MexcPrimary,
                            unfocusedBorderColor = MexcBorder,
                            focusedTextColor = MexcTextPrimary,
                            unfocusedTextColor = MexcTextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trade_amount_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // High-speed execute button
                    Button(
                        onClick = onExecuteTrade,
                        colors = ButtonDefaults.buttonColors(containerColor = actionColor),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !uiState.isExecutingTrade,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("execute_trade_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.FlashOn, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isBuy) "BUY ${selected.baseAsset}" else "SELL ${selected.baseAsset}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Recent Executed Orders Section
        item {
            Text(
                text = "Order History & Executions",
                color = MexcTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
            )
        }

        if (uiState.orders.isEmpty()) {
            item {
                Text("No orders placed yet. Execute a trade above to view instant filled logs.", color = MexcTextSecondary, fontSize = 12.sp)
            }
        } else {
            items(uiState.orders) { order ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MexcSurface),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .testTag("order_item_${order.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = order.type,
                                    color = if (order.type == "BUY") MexcGreen else MexcRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(order.symbol, color = MexcTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Text("Amt: ${order.amount} @ $${order.price}", color = MexcTextSecondary, fontSize = 11.sp)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Surface(
                                color = MexcGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${order.status} (${order.executionLatencyMs}ms)",
                                    color = MexcGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text("Total: $${String.format("%.2f", order.totalUsdt)}", color = MexcTextPrimary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
