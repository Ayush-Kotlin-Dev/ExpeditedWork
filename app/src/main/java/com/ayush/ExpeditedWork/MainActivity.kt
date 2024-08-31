package com.ayush.ExpeditedWork

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import com.ayush.ExpeditedWork.ui.theme.ExpeditedWorkTheme
import com.ketch.DownloadConfig
import com.ketch.Ketch
import com.ketch.NotificationConfig
import java.time.Duration



class MainActivity : ComponentActivity() {
    private lateinit var ketch : Ketch
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ketch = Ketch.builder().build(this)
        enableEdgeToEdge()
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)

        }
        ketch = Ketch.builder()
            .setNotificationConfig(
                NotificationConfig(
                    enabled = true,
                    smallIcon = R.drawable.ic_launcher_foreground,
                )
            )
            .setDownloadConfig(
                DownloadConfig(
                    connectTimeOutInMs = 10000,
                    readTimeOutInMs = 10000,
                )
            )
            .build(this)

        setContent {
            ExpeditedWorkTheme {
                expeditiousWork(context = this)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(ketch = ketch)

                }
            }
        }
    }
}


fun expeditiousWork(context: Context) {
    val workRequest = OneTimeWorkRequestBuilder<CustomWorker>()
        .setExpedited(OutOfQuotaPolicy.DROP_WORK_REQUEST)
        .setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            duration = Duration.ofSeconds(10)
        )
        .build()
    androidx.work.WorkManager.getInstance(context).enqueue(workRequest)
}
