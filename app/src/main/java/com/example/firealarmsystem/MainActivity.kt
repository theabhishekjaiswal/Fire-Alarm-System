package com.example.firealarmsystem

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var fireStatus: TextView
    private lateinit var database: DatabaseReference
    private lateinit var backgroundLayout: LinearLayout
    private lateinit var alarmimg: ImageView
    private lateinit var mediaPlayer: MediaPlayer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fireStatus = findViewById(R.id.fireStatus)
        backgroundLayout = findViewById(R.id.backgroundLayout)
        alarmimg = findViewById(R.id.alarmimg)
        database = FirebaseDatabase.getInstance().reference

        mediaPlayer = MediaPlayer.create(this, R.raw.fire_alarm)

        // Schedule the initial FireAlarmWorker
        val workRequest = OneTimeWorkRequestBuilder<FireAlarmWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)

        // Existing Firebase listener setup (optional)
        database.child("fireDetected").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fireDetected = snapshot.getValue(Boolean::class.java) ?: false
                if (fireDetected) {
                    fireStatus.text = "Fire Detected!"
                    alarmimg.visibility = View.VISIBLE
                    backgroundLayout.setBackgroundColor(resources.getColor(R.color.red))
                    fireStatus.setTextColor(resources.getColor(R.color.white))
                    if (!mediaPlayer.isPlaying) {
                        mediaPlayer.start()
                    }
                } else {
                    alarmimg.visibility = View.GONE
                    fireStatus.text = "Safe!"
                    backgroundLayout.setBackgroundColor(resources.getColor(R.color.white))
                    fireStatus.setTextColor(resources.getColor(R.color.black))
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}
