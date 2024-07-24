package com.example.firealarmsystem

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.database.*

class FireAlarmService : Service() {

    private lateinit var database: DatabaseReference
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        // Create notification channel for foreground service
        createNotificationChannel()

        // Set up Firebase listener
        setupFirebaseListener()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "FireAlarmServiceChannel",
                "Fire Alarm Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "This channel is used for fire alarm notifications."
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun setupFirebaseListener() {
        database.child("fireDetected").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fireDetected = snapshot.getValue(Boolean::class.java) ?: false
                if (fireDetected) {
                    triggerAlarm()
                }else{
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Log.e("FireAlarmService", "Firebase error: ${error.message}")
            }
        })
    }

    private fun triggerAlarm() {
        // Create notification
        val notification = NotificationCompat.Builder(this, "FireAlarmServiceChannel")
            .setContentTitle("Fire Alarm")
            .setContentText("Fire detected! Take immediate action.")
            .setSmallIcon(R.drawable.firealarmsystemicon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.fire_alarm))
            .build()

        // Show notification
        val manager = getSystemService(NotificationManager::class.java)
        manager?.notify(1, notification)

        // Play specific music file
        mediaPlayer = MediaPlayer.create(this, R.raw.fire_alarm)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
