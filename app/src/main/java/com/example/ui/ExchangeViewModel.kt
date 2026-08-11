package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.entity.OrderEntity
import com.example.data.entity.SecurityAuditLogEntity
import com.example.data.entity.SecuritySettingsEntity
import com.example.data.entity.WalletAssetEntity
import com.example.data.model.Candlestick
import com.example.data.model.CryptoTicker
import com.example.data.model.OrderBookData
import com.example.data.repository.ExchangeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class NavigationTab(val route: String, val title: String) {
    object Home : NavigationTab("home", "Home")
    object Markets : NavigationTab("markets", "Markets")
    object Trade : NavigationTab("trade", "Trade")
    object Futures : NavigationTab("futures", "Futures")
    object Wallet : NavigationTab("wallet", "Wallet")
    object Security : NavigationTab("security", "Security")
}

data class ExchangeUiState(
    val selectedTab: NavigationTab = NavigationTab.Home,
    val selectedTicker: CryptoTicker = CryptoTicker("BTC/USDT", "BTC", "USDT", 94820.50, 3.82, 96200.0, 92100.0, 1824000000.0, true, true, "Spot"),
    val tickers: List<CryptoTicker> = emptyList(),
    val marketCategoryFilter: String = "All", // "All", "Spot", "Futures", "Meme", "Gainers"
    val searchQuery: String = "",
    val orderBook: OrderBookData? = null,
    val candlesticks: List<Candlestick> = emptyList(),
    val orders: List<OrderEntity> = emptyList(),
    val walletAssets: List<WalletAssetEntity> = emptyList(),
    val securitySettings: SecuritySettingsEntity? = null,
    val auditLogs: List<SecurityAuditLogEntity> = emptyList(),
    val currentTotpCode: String = "849 203",
    val totpRemainingSeconds: Int = 24,
    val lastExecutionLatencyMs: Int? = null,
    val isExecutingTrade: Boolean = false,
    val activeTradeType: String = "BUY", // "BUY" or "SELL"
    val activeOrderMode: String = "LIMIT", // "LIMIT" or "MARKET"
    val tradePriceInput: String = "94820.50",
    val tradeAmountInput: String = "0.05",
    val futuresLeverage: Int = 20, // 1x to 125x
    val isColdStorageModalOpen: Boolean = false,
    val transferAssetSymbol: String = "BTC",
    val transferAmountInput: String = "0.5",
    val userEntered2FACode: String = "",
    val notificationMessage: String? = null
)

class ExchangeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = ExchangeRepository(db.orderDao(), db.walletDao(), db.securityDao())

    private val _uiState = MutableStateFlow(ExchangeUiState())
    val uiState: StateFlow<ExchangeUiState> = _uiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.initializeDefaultWallet()
            repository.initializeDefaultSecurity()
        }

        // Live Market Ticker Stream
        viewModelScope.launch {
            repository.getLiveTickersFlow().collectLatest { tickerList ->
                val currentSelected = _uiState.value.selectedTicker
                val updatedSelected = tickerList.find { it.symbol == currentSelected.symbol } ?: currentSelected
                val orderBook = repository.getOrderBook(updatedSelected.price)

                _uiState.value = _uiState.value.copy(
                    tickers = tickerList,
                    selectedTicker = updatedSelected,
                    orderBook = orderBook
                )
            }
        }

        // Load Orders
        viewModelScope.launch {
            repository.getAllOrders().collectLatest { orderList ->
                _uiState.value = _uiState.value.copy(orders = orderList)
            }
        }

        // Load Wallet Assets
        viewModelScope.launch {
            repository.getWalletAssets().collectLatest { assets ->
                _uiState.value = _uiState.value.copy(walletAssets = assets)
            }
        }

        // Load Security Settings
        viewModelScope.launch {
            repository.getSecuritySettings().collectLatest { settings ->
                _uiState.value = _uiState.value.copy(securitySettings = settings)
            }
        }

        // Load Audit Logs
        viewModelScope.launch {
            repository.getAuditLogs().collectLatest { logs ->
                _uiState.value = _uiState.value.copy(auditLogs = logs)
            }
        }

        // 2FA TOTP Code dynamic ticker loop (30s refresh)
        viewModelScope.launch {
            var secondsLeft = 30
            while (true) {
                delay(1000)
                secondsLeft -= 1
                if (secondsLeft <= 0) {
                    secondsLeft = 30
                    val newCode = "${Random.nextInt(100, 999)} ${Random.nextInt(100, 999)}"
                    _uiState.value = _uiState.value.copy(currentTotpCode = newCode)
                }
                _uiState.value = _uiState.value.copy(totpRemainingSeconds = secondsLeft)
            }
        }

        // Load Candlesticks for initial selected pair
        updateSelectedTicker(_uiState.value.selectedTicker)
    }

    fun selectTab(tab: NavigationTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun updateSelectedTicker(ticker: CryptoTicker) {
        val candles = repository.getCandlesticks(ticker.price)
        val orderBook = repository.getOrderBook(ticker.price)
        _uiState.value = _uiState.value.copy(
            selectedTicker = ticker,
            candlesticks = candles,
            orderBook = orderBook,
            tradePriceInput = if (ticker.price < 1.0) String.format("%.6f", ticker.price) else String.format("%.2f", ticker.price)
        )
    }

    fun setMarketCategoryFilter(filter: String) {
        _uiState.value = _uiState.value.copy(marketCategoryFilter = filter)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setTradeType(type: String) { // "BUY" or "SELL"
        _uiState.value = _uiState.value.copy(activeTradeType = type)
    }

    fun setOrderMode(mode: String) { // "LIMIT" or "MARKET"
        _uiState.value = _uiState.value.copy(activeOrderMode = mode)
    }

    fun setTradePriceInput(price: String) {
        _uiState.value = _uiState.value.copy(tradePriceInput = price)
    }

    fun setTradeAmountInput(amount: String) {
        _uiState.value = _uiState.value.copy(tradeAmountInput = amount)
    }

    fun setFuturesLeverage(leverage: Int) {
        _uiState.value = _uiState.value.copy(futuresLeverage = leverage)
    }

    fun executeTrade() {
        val currentState = _uiState.value
        val price = currentState.tradePriceInput.toDoubleOrNull() ?: currentState.selectedTicker.price
        val amount = currentState.tradeAmountInput.toDoubleOrNull() ?: 0.01

        if (amount <= 0) {
            viewModelScope.launch { _toastEvent.emit("Please enter a valid amount to trade.") }
            return
        }

        _uiState.value = currentState.copy(isExecutingTrade = true)

        viewModelScope.launch {
            val executedOrder = repository.executeLowLatencyTrade(
                symbol = currentState.selectedTicker.symbol,
                type = currentState.activeTradeType,
                orderMode = currentState.activeOrderMode,
                price = price,
                amount = amount
            )

            _uiState.value = _uiState.value.copy(
                isExecutingTrade = false,
                lastExecutionLatencyMs = executedOrder.executionLatencyMs,
                notificationMessage = "Trade executed in ${executedOrder.executionLatencyMs}ms latency! ${executedOrder.type} ${executedOrder.amount} ${executedOrder.symbol}"
            )
            _toastEvent.emit("Order Filled! Matched in ${executedOrder.executionLatencyMs}ms")
        }
    }

    fun openColdStorageModal(assetSymbol: String = "BTC") {
        _uiState.value = _uiState.value.copy(
            isColdStorageModalOpen = true,
            transferAssetSymbol = assetSymbol,
            userEntered2FACode = ""
        )
    }

    fun closeColdStorageModal() {
        _uiState.value = _uiState.value.copy(isColdStorageModalOpen = false)
    }

    fun setTransferAmountInput(amount: String) {
        _uiState.value = _uiState.value.copy(transferAmountInput = amount)
    }

    fun setUser2FACode(code: String) {
        _uiState.value = _uiState.value.copy(userEntered2FACode = code)
    }

    fun submitColdStorageTransfer() {
        val state = _uiState.value
        val amount = state.transferAmountInput.toDoubleOrNull() ?: 0.0

        if (amount <= 0) {
            viewModelScope.launch { _toastEvent.emit("Please enter a valid transfer amount.") }
            return
        }

        if (state.securitySettings?.isTwoFactorEnabled == true && state.userEntered2FACode.length < 4) {
            viewModelScope.launch { _toastEvent.emit("Security Check: Please enter 2FA verification code.") }
            return
        }

        viewModelScope.launch {
            val success = repository.transferToColdStorage(state.transferAssetSymbol, amount)
            if (success) {
                _uiState.value = _uiState.value.copy(
                    isColdStorageModalOpen = false,
                    notificationMessage = "Successfully transferred $amount ${state.transferAssetSymbol} to Cold Storage Vault."
                )
                _toastEvent.emit("Transferred $amount ${state.transferAssetSymbol} to Air-Gapped Cold Vault!")
            } else {
                _toastEvent.emit("Insufficient Hot Wallet balance for transfer.")
            }
        }
    }

    fun toggleTwoFactorAuth() {
        val current = _uiState.value.securitySettings ?: return
        val updated = current.copy(isTwoFactorEnabled = !current.isTwoFactorEnabled)
        viewModelScope.launch {
            repository.updateSecuritySettings(updated)
            _toastEvent.emit("2FA Security level updated.")
        }
    }

    fun toggleBiometrics() {
        val current = _uiState.value.securitySettings ?: return
        val updated = current.copy(isBiometricsEnabled = !current.isBiometricsEnabled)
        viewModelScope.launch {
            repository.updateSecuritySettings(updated)
            _toastEvent.emit("Biometric Hardware Protection toggled.")
        }
    }

    fun dismissNotification() {
        _uiState.value = _uiState.value.copy(notificationMessage = null)
    }
}
