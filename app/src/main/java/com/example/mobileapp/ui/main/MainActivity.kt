package com.example.mobileapp.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapp.R
import com.example.mobileapp.ui.second.SecondActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkNotificationPermission()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val etFirstText = findViewById<EditText>(R.id.etFirstText)
        val etSecondText = findViewById<EditText>(R.id.etSecondText)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnNext = findViewById<Button>(R.id.btnNext)
        listView = findViewById(R.id.lvItems)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList())
        listView.adapter = adapter

        viewModel.allItems.observe(this) { items ->
            val itemTexts = items.map { "${it.firstText} - ${it.secondText}" }
            adapter.clear()
            adapter.addAll(itemTexts)
            adapter.notifyDataSetChanged()
        }

        btnAdd.setOnClickListener {
            val firstText = etFirstText.text.toString()
            val secondText = etSecondText.text.toString()
            if (firstText.isNotEmpty() && secondText.isNotEmpty()) {
                viewModel.insertItem(firstText, secondText)
                etFirstText.text.clear()
                etSecondText.text.clear()
            } else {
                Toast.makeText(this, "Please enter both texts", Toast.LENGTH_SHORT).show()
            }
        }

        btnClear.setOnClickListener {
            viewModel.clearItems()
        }

        btnNext.setOnClickListener {
            val firstText = etFirstText.text.toString()
            val secondText = etSecondText.text.toString()
            if (firstText.isNotEmpty() && secondText.isNotEmpty()) {
                val intent = Intent(this, SecondActivity::class.java).apply {
                    putExtra("firstText", firstText)
                    putExtra("secondText", secondText)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Enter texts before proceeding", Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            Toast.makeText(this, selectedItem, Toast.LENGTH_SHORT).show()
        }

        createNotificationChannel()

        viewModel.pauseCount.observe(this) { count ->
            showCustomNotification(count)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.incrementPauseCount()
        showNotification(viewModel.pauseCount.value ?: 0)
        Log.d("NotificationTest", "onPause called")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "pause_channel",
                "Pause Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "This channel shows pause notifications"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermission() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel("pause_channel")
            if (channel?.importance == NotificationManager.IMPORTANCE_NONE) {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    putExtra(Settings.EXTRA_CHANNEL_ID, "pause_channel")
                }
                startActivity(intent)
            }
        }
    }

    private fun showCustomNotification(count: Int) {
        val notificationLayout = RemoteViews(packageName, R.layout.custom_notification)
        notificationLayout.setTextViewText(R.id.tvContent, "Paused count: $count")

        val notification = NotificationCompat.Builder(this, "pause_channel")
            .setSmallIcon(R.drawable.icon_notification)
            .setCustomContentView(notificationLayout)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(1, notification)
    }

    private fun showNotification(count: Int) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        Log.d("NotificationTest", "Notification count: $count")

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "pause_channel")
            .setSmallIcon(R.drawable.icon_notification)
            .setContentTitle("App Paused")
            .setContentText("Paused count: $count")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}