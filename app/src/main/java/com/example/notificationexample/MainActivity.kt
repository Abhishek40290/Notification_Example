package com.example.notificationexample

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "channel_id"
    private val CHANNEL_NAME= "channel_name"
    private val NOTIFICATION_ID = 0
    val PERMISSION_REQUEST_CODE = 101
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        val intent = Intent(this,MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Example Title")
            .setContentText("Example Description")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Hello there , the notification feature is working properly no need to worry."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)


        binding.btnButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        }

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
              val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                  NotificationManager.IMPORTANCE_DEFAULT).apply {
                  lightColor = Color.GREEN
                  enableLights(true)
            }
            val manager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}