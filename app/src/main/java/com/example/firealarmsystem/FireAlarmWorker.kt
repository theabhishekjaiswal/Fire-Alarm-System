package com.example.firealarmsystem

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.WorkManager
import com.google.firebase.database.*
import java.util.concurrent.TimeUnit

class FireAlarmWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            // Process the database
            processtheDb()
            // Schedule the next work
            scheduleNextWork()
            Result.success()
        } catch (e: Exception) {
            Log.e("FireCheckWorker", "Exception in doWork: ${e.message}")
            Result.failure()
        }
    }

    private fun scheduleNextWork() {
        val workRequest = OneTimeWorkRequestBuilder<FireAlarmWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }

    private fun sendNotification(messageBody: String) {
        try {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext, 0, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )

            val channelId = "FireAlarmChannel"
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.firealarmsystemicon)
                .setContentTitle("Fire Alarm")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Fire Alarm Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0, notificationBuilder.build())
        } catch (e: Exception) {
            Log.e("FireCheckWorker", "Exception in sendNotification: ${e.message}")
        }
    }

    private fun processtheDb() {
        val database = FirebaseDatabase.getInstance().reference
        val fireDetectedRef = database.child("fireDetected")

        fireDetectedRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fireDetected = snapshot.getValue(Boolean::class.java) ?: false
                if (fireDetected) {
                    sendNotification("Fire detected! Please take necessary action.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FireCheckWorker", "Database error: ${error.message}")
            }
        })
    }
}
