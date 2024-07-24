package com.example.firealarmsystem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class FireAlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MyBroadcastReceiver", "Broadcast received: ${intent.action}")

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_REBOOT -> {
                // Reschedule the periodic worker when the device is booted or rebooted
                try {
                    val workRequest = OneTimeWorkRequestBuilder<FireAlarmWorker>()
                        .setInitialDelay(1, TimeUnit.MINUTES)
                        .build()
                    WorkManager.getInstance(context).enqueue(workRequest)
                } catch (e: Exception) {
                    Log.e("MyBroadcastReceiver", "Failed to enqueue work: ${e.message}")
                }
            }
            Intent.ACTION_AIRPLANE_MODE_CHANGED, ConnectivityManager.CONNECTIVITY_ACTION -> {
                // Handle network changes
                if (isNetworkAvailable(context)) {
                    Log.d("MyBroadcastReceiver", "Network available. Rescheduling worker.")
                    try {
                        val workRequest = OneTimeWorkRequestBuilder<FireAlarmWorker>()
                            .setInitialDelay(1, TimeUnit.MINUTES)
                            .build()
                        WorkManager.getInstance(context).enqueue(workRequest)
                    } catch (e: Exception) {
                        Log.e("MyBroadcastReceiver", "Failed to enqueue work: ${e.message}")
                    }
                } else {
                    Log.d("MyBroadcastReceiver", "Network not available.")
                }
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        }
    }
}

