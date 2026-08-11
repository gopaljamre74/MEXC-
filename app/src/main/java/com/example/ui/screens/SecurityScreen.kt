package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phishing
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ui.theme.MexcAccentGold
import com.example.ui.theme.MexcBorder
import com.example.ui.theme.MexcColdBlue
import com.example.ui.theme.MexcGreen
import com.example.ui.theme.MexcPrimary
import com.example.ui.theme.MexcSurface
import com.example.ui.theme.MexcSurfaceVariant
import com.example.ui.theme.MexcTextPrimary
import com.example.ui.theme.MexcTextSecondary

@Composable
fun SecurityScreen(
    uiState: ExchangeUiState,
    onToggle2FA: () -> Unit,
    onToggleBiometrics: () -> Unit,
    onOpenColdStorageModal: () -> Unit,
    onCloseColdStorageModal: () -> Unit,
    onTransferAmountChange: (String) -> Unit,
    onUser2FACodeChange: (String) -> Unit,
    onSubmitColdStorageTransfer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val security = uiState.securitySettings

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("security_screen")
    ) {
        // Header Section
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(MexcPrimary.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Security Shield",
                        tint = MexcPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Security Configuration",
                        color = MexcTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Hardware Cold Vault & Multi-Factor Protection",
                        color = MexcTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Security Status Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MexcSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("security_status_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = MexcGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Security Status: ULTRA SECURE",
                                color = MexcGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Surface(
                            color = MexcGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Score: 98/100",
                                color = MexcGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Your account is protected by hardware biometrics, time-based 2FA, anti-phishing hashes, and cold storage isolation.",
                        color = MexcTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = MexcColdBlue.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MexcColdBlue.copy(alpha = 0.3f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AcUnit,
                                    contentDescription = null,
                                    tint = MexcColdBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Cold Storage Vault",
                                    color = MexcColdBlue,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            color = MexcGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MexcGreen.copy(alpha = 0.3f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MexcGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "2FA Active",
                                    color = MexcGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live TOTP Authenticator Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MexcSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("totp_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = MexcAccentGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Time-Based Authenticator (TOTP)",
                                color = MexcTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "${uiState.totpRemainingSeconds}s remaining",
                            color = MexcAccentGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MexcSurfaceVariant, RoundedCornerShape(10.dp))
                            .border(1.dp, MexcBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.currentTotpCode,
                            color = MexcTextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 4.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { uiState.totpRemainingSeconds / 30f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = MexcAccentGold,
                        trackColor = MexcSurfaceVariant
                    )
                }
            }
        }

        // Security Toggles Section
        item {
            Text(
                text = "Authentication & Access Controls",
                color = MexcTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // 2FA Toggle
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
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = MexcPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Two-Factor Authentication (2FA)", color = MexcTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Requires 6-digit TOTP code for withdrawals & trades", color = MexcTextSecondary, fontSize = 11.sp)
                        }
                    }

                    Switch(
                        checked = security?.isTwoFactorEnabled == true,
                        onCheckedChange = { onToggle2FA() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MexcGreen,
                            uncheckedThumbColor = MexcTextSecondary,
                            uncheckedTrackColor = MexcSurfaceVariant
                        ),
                        modifier = Modifier.testTag("toggle_2fa_switch")
                    )
                }
            }
        }

        // Biometrics Toggle
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
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, tint = MexcColdBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Hardware Biometric Lock", color = MexcTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Fingerprint & FaceUnlock for app launch", color = MexcTextSecondary, fontSize = 11.sp)
                        }
                    }

                    Switch(
                        checked = security?.isBiometricsEnabled == true,
                        onCheckedChange = { onToggleBiometrics() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MexcGreen,
                            uncheckedThumbColor = MexcTextSecondary,
                            uncheckedTrackColor = MexcSurfaceVariant
                        ),
                        modifier = Modifier.testTag("toggle_biometrics_switch")
                    )
                }
            }
        }

        // Anti-Phishing Code Card
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
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.Phishing, contentDescription = null, tint = MexcAccentGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Anti-Phishing Code Hash", color = MexcTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Code: ${security?.antiPhishingCode ?: "MEXC_SAFE_2026"}", color = MexcTextSecondary, fontSize = 11.sp)
                        }
                    }

                    Surface(
                        color = MexcAccentGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "VERIFIED",
                            color = MexcAccentGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Cold Storage Vault Action Card
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MexcSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MexcColdBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, tint = MexcColdBlue, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Air-Gapped Cold Vault", color = MexcTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Transfer assets from hot wallet to offline cold storage vault protected by multi-signature threshold cryptography.",
                        color = MexcTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onOpenColdStorageModal,
                        colors = ButtonDefaults.buttonColors(containerColor = MexcColdBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_cold_storage_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Transfer to Cold Storage Vault", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Security Audit Log Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Security Audit Trail", color = MexcTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.History, contentDescription = null, tint = MexcTextSecondary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Live Logs", color = MexcTextSecondary, fontSize = 11.sp)
                }
            }
        }

        if (uiState.auditLogs.isEmpty()) {
            item {
                Text("No audit events recorded recently.", color = MexcTextSecondary, fontSize = 12.sp)
            }
        } else {
            items(uiState.auditLogs) { log ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MexcSurface),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MexcBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .testTag("audit_log_item_${log.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(log.action, color = MexcTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("IP: ${log.ipAddress} • ${log.deviceName}", color = MexcTextSecondary, fontSize = 11.sp)
                        }
                        Surface(
                            color = MexcGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = log.status,
                                color = MexcGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Cold Storage Transfer Modal
    if (uiState.isColdStorageModalOpen) {
        AlertDialog(
            onDismissRequest = onCloseColdStorageModal,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, tint = MexcColdBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cold Vault Deposit", color = MexcTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        "Transfer asset (${uiState.transferAssetSymbol}) to air-gapped cold storage. Requires 2FA verification.",
                        color = MexcTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.transferAmountInput,
                        onValueChange = onTransferAmountChange,
                        label = { Text("Transfer Amount (${uiState.transferAssetSymbol})") },
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
                            .testTag("cold_transfer_amount_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.userEntered2FACode,
                        onValueChange = onUser2FACodeChange,
                        label = { Text("6-Digit 2FA Code") },
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
                            .testTag("cold_transfer_2fa_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onSubmitColdStorageTransfer,
                    colors = ButtonDefaults.buttonColors(containerColor = MexcColdBlue),
                    modifier = Modifier.testTag("submit_cold_transfer_button")
                ) {
                    Text("Confirm Transfer", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onCloseColdStorageModal) {
                    Text("Cancel", color = MexcTextSecondary)
                }
            },
            containerColor = MexcSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
