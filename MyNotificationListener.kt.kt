package com.example.myapplication

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyNotificationListener : NotificationListenerService() {

    private val paymentRegex = Regex("(.+?)\\s+([\\d,]+)원")

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return
        if ((sbn.notification.flags and Notification.FLAG_GROUP_SUMMARY) != 0) return

        val text = sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: return
        val matchResult = paymentRegex.find(text) ?: return

        try {
            val merchant = matchResult.groupValues[1].replace("에서", "").trim()
            val amount = matchResult.groupValues[2].replace(",", "").toInt()
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

            val parsed = ParsedPayment(
                merchant = merchant,
                amount = amount,
                time = currentTime
            )

            PaymentStore.addPayment(parsed)
            Log.d("NotiTest", "화면 누적 추가 완료: $parsed")
        } catch (e: Exception) {
            Log.e("NotiTest", "파싱 실패: ${e.message}")
        }
    }
}