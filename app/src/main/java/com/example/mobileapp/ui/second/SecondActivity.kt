package com.example.mobileapp.ui.second

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapp.R

class SecondActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val tvReversedText = findViewById<TextView>(R.id.tvReversedText)

        val firstText = intent.getStringExtra("firstText") ?: ""
        val secondText = intent.getStringExtra("secondText") ?: ""

        val reversedFirst = firstText.reversed()
        val reversedSecond = secondText.reversed()

        tvReversedText.text = "Reversed:\n$reversedFirst\n$reversedSecond"
    }
}