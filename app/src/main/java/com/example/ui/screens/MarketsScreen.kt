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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoTicker
import com.example.ui.ExchangeUiState
import com.example.ui.NavigationTab
import com.example.ui.components.CryptoTickerRow
import com.example.ui.theme.MexcBorder
import com.example.ui.theme.MexcPrimary
import com.example.ui.theme.MexcSurface
import com.example.ui.theme.MexcSurfaceVariant
import com.example.ui.theme.MexcTextPrimary
import com.example.ui.theme.MexcTextSecondary

@Composable
fun MarketsScreen(
    uiState: ExchangeUiState,
    onSearchQueryChange: (String) -> Unit,
    onCategoryFilterSelect: (String) -> Unit,
    onSelectTicker: (CryptoTicker) -> Unit,
    onNavigateTab: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Spot", "Futures", "Meme", "Gainers")

    val filteredTickers = uiState.tickers.filter { ticker ->
        val matchesSearch = ticker.symbol.contains(uiState.searchQuery, ignoreCase = true) ||
                ticker.baseAsset.contains(uiState.searchQuery, ignoreCase = true)

        val matchesCategory = when (uiState.marketCategoryFilter) {
            "All" -> true
            "Spot" -> ticker.category == "Spot"
            "Futures" -> ticker.category == "Futures"
            "Meme" -> ticker.category == "Meme"
            "Gainers" -> ticker.isGainer
            else -> true
        }

        matchesSearch && matchesCategory
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("markets_screen")
    ) {
        // Top Header Search Field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search crypto pair e.g. BTC, ETH, MX", color = MexcTextSecondary, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = MexcTextSecondary)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = MexcTextSecondary)
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MexcSurface,
                    unfocusedContainerColor = MexcSurface,
                    focusedBorderColor = MexcPrimary,
                    unfocusedBorderColor = MexcBorder,
                    focusedTextColor = MexcTextPrimary,
                    unfocusedTextColor = MexcTextPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("markets_search_input")
            )
        }

        // Category Filter Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                val isSelected = uiState.marketCategoryFilter == category
                Surface(
                    color = if (isSelected) MexcPrimary else MexcSurface,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) MexcPrimary else MexcBorder),
                    modifier = Modifier
                        .clickable { onCategoryFilterSelect(category) }
                        .testTag("category_chip_$category")
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.Black else MexcTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Table Column Headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MexcSurfaceVariant)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Name / Vol", color = MexcTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("Last Price", color = MexcTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("24h Chg%", color = MexcTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        // Markets Ticker Rows
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredTickers) { ticker ->
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
}
