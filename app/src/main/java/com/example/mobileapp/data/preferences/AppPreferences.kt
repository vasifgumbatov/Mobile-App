package com.example.mobileapp.data.preferences

import android.content.Context

class AppPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun getPauseCount(): Int {
        return sharedPreferences.getInt("pause_count", 0)
    }

    fun incrementPauseCount() {
        val count = getPauseCount() + 1
        sharedPreferences.edit().putInt("pause_count", count).apply()
    }
}
