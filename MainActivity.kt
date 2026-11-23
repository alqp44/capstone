package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NotiTestScreen()
            }
        }
    }
}

@Composable
fun NotiTestScreen() {
    val context = LocalContext.current
    val paymentList by PaymentStore.payments.collectAsState()
    val numberFormat = remember { NumberFormat.getNumberInstance(Locale.KOREA) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                }
            ) {
                Text("알림 권한 설정")
            }

            OutlinedButton(
                onClick = {
                    val now = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    PaymentStore.addPayment(
                        ParsedPayment(
                            merchant = "스타벅스",
                            amount = 5000,
                            time = now
                        )
                    )
                }
            ) {
                Text("테스트 추가")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "결제 알림 수집기 (${paymentList.size}건)",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(paymentList) { item ->
                val formattedAmount = "${numberFormat.format(item.amount)}원"
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${item.merchant} / $formattedAmount / ${item.time}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}