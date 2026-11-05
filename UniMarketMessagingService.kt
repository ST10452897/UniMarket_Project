package za.co.unimarket.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import za.co.unimarket.MainActivity
import za.co.unimarket.R
import kotlin.random.Random

class UniMarketMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token received")

        // Store the FCM token locally so it can be sent to the authenticated backend
        val prefs = applicationContext.getSharedPreferences("unimarket_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("fcm_token", token).apply()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Respect the user's notification toggle from Settings (defaults to true)
        val settingsPrefs = applicationContext.getSharedPreferences("unimarket_settings", Context.MODE_PRIVATE)
        if (!settingsPrefs.getBoolean("notifications_enabled", true)) {
            Log.d(TAG, "Push notification suppressed because user disabled notifications in Settings.")
            return
        }

        val title = message.notification?.title
            ?: message.data["title"]
            ?: "UniMarket"

        val body = message.notification?.body
            ?: message.data["body"]
            ?: "You have a new update."

        Log.d(TAG, "$title: $body")

        showSystemNotification(title, body)
    }

    private fun showSystemNotification(title: String, body: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                Log.w(TAG, "POST_NOTIFICATIONS permission not granted; skipping system notification display.")
                return
            }
        }

        val channelId = "unimarket_notifications_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "UniMarket Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Marketplace listing alerts and buyer/seller messages"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            pendingIntentFlags
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(Random.nextInt(1000, 999999), notification)
    }

    companion object {
        private const val TAG = "UniMarketFCM"
    }
}
