package com.example.mobileapp.data.preferences

import android.content.Context

class AppPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun setPauseCount(count: Int) {
        prefs.edit().putInt("pause_count", count).apply()
    }

    fun getPauseCount(): Int {
        return prefs.getInt("pause_count", 0)
    }
}

