package com.example.buddyapp.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.buddyapp.R
import kotlin.random.Random

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val messages = listOf(
        "Ceritakan kisahmu hari ini yuk!",
        "Jangan lupa untuk memeriksa kesehatan mentalmu hari ini!",
        "Apa kabar? Coba luangkan waktu untuk diri sendiri sejenak.",
        "Saatnya untuk berhenti sejenak dan refleksikan perasaanmu.",
        "Kamu tidak sendirian. Cek kesehatan mentalmu hari ini!",
        "Yuk istirahat sebentar dan ceritakan perasaanmu hari ini."
    )

    override fun doWork(): Result {
        Log.d("NotificationWorker", "doWork() started")
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    return Result.failure()
                }
            }

            val randomMessage = messages[Random.nextInt(messages.size)]

            val notificationId = Random.nextInt(1000)
            val channelId = "mental_health_channel"

            val notification = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.buddy_ad_register)
                .setContentTitle("Hallo! Buddy disini untuk kamu.")
                .setContentText(randomMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSilent(true)
                .build()

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(notificationId, notification)
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}