package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Candlestick
import com.example.data.model.CryptoTicker
import com.example.data.model.OrderBookData
import com.example.ui.theme.MexcBorder
import com.example.ui.theme.MexcColdBlue
import com.example.ui.theme.MexcGreen
import com.example.ui.theme.MexcRed
import com.example.ui.theme.MexcSurface
import com.example.ui.theme.MexcSurfaceVariant
import com.example.ui.theme.MexcTextPrimary
import com.example.ui.theme.MexcTextSecondary

@Composable
fun LatencyGaugeBadge(latencyMs: Int? = 12) {
    val ms = latencyMs ?: 14
    val color = if (ms < 30) MexcGreen else Color(0xFFFFB800)
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f)),
        modifier = Modifier.testTag("latency_gauge_badge")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FlashOn,
                contentDescription = "Ultra Low Latency Engine",
                tint = color,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Execution: ${ms}ms",
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun NotificationBanner(
    message: String,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = message.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            color = MexcGreen.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, MexcGreen),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("notification_banner")
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MexcGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = message,
                    color = MexcTextPrimary,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MexcTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CryptoTickerRow(
    ticker: CryptoTicker,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPositive = ticker.change24h >= 0
    val changeColor = if (isPositive) MexcGreen else MexcRed
    val formattedPrice = if (ticker.price < 1.0) String.format("%.6f", ticker.price) else String.format("%.2f", ticker.price)
    val formattedChange = "${if (isPositive) "+" else ""}${String.format("%.2f", ticker.change24h)}%"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("ticker_row_${ticker.symbol.replace("/", "_")}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MexcSurfaceVariant)
                    .border(1.dp, MexcBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ticker.baseAsset.take(3),
                    color = MexcTextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = ticker.symbol,
                        color = MexcTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (ticker.isHot) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            color = Color(0xFFFF5252).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "HOT",
                                color = Color(0xFFFF5252),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "Vol $${(ticker.volume24hUsdt / 1_000_000).toInt()}M",
                    color = MexcTextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$$formattedPrice",
                color = MexcTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Surface(
                color = changeColor,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = formattedChange,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Composable
fun CandlestickChart(
    candles: List<Candlestick>,
    modifier: Modifier = Modifier
) {
    if (candles.isEmpty()) return

    val minLow = candles.minOf { it.low }
    val maxHigh = candles.maxOf { it.high }
    val priceRange = maxOf(0.0001, maxHigh - minLow)

    Card(
        colors = CardDefaults.cardColors(containerColor = MexcSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp)
            .testTag("candlestick_chart")
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val candleWidth = width / candles.size
                val bodyWidth = maxOf(2f, candleWidth * 0.6f)

                // Grid lines
                for (i in 1..3) {
                    val y = height * (i / 4f)
                    drawLine(
                        color = MexcBorder.copy(alpha = 0.5f),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                }

                candles.forEachIndexed { index, candle ->
                    val x = index * candleWidth + candleWidth / 2f
                    val highY = (height - ((candle.high - minLow) / priceRange * height)).toFloat()
                    val lowY = (height - ((candle.low - minLow) / priceRange * height)).toFloat()
                    val openY = (height - ((candle.open - minLow) / priceRange * height)).toFloat()
                    val closeY = (height - ((candle.close - minLow) / priceRange * height)).toFloat()

                    val isBullish = candle.close >= candle.open
                    val color = if (isBullish) MexcGreen else MexcRed

                    // Wick
                    drawLine(
                        color = color,
                        start = Offset(x, highY),
                        end = Offset(x, lowY),
                        strokeWidth = 2f
                    )

                    // Body
                    val top = minOf(openY, closeY)
                    val bottom = maxOf(openY, closeY)
                    val bodyHeight = maxOf(2f, bottom - top)

                    drawRect(
                        color = color,
                        topLeft = Offset(x - bodyWidth / 2f, top),
                        size = Size(bodyWidth, bodyHeight)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderBookWidget(
    orderBookData: OrderBookData?,
    modifier: Modifier = Modifier
) {
    if (orderBookData == null) return

    Card(
        colors = CardDefaults.cardColors(containerColor = MexcSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .testTag("orderbook_widget")
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Price (USDT)", color = MexcTextSecondary, fontSize = 10.sp)
                Text("Amount", color = MexcTextSecondary, fontSize = 10.sp)
            }

            // Asks (Red)
            orderBookData.asks.take(5).forEach { ask ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (ask.price < 1.0) String.format("%.6f", ask.price) else String.format("%.2f", ask.price),
                        color = MexcRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = String.format("%.3f", ask.amount),
                        color = MexcTextPrimary,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Bids (Green)
            orderBookData.bids.take(5).forEach { bid ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (bid.price < 1.0) String.format("%.6f", bid.price) else String.format("%.2f", bid.price),
                        color = MexcGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = String.format("%.3f", bid.amount),
                        color = MexcTextPrimary,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
