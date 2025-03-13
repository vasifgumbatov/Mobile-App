package com.example.mobileapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_items")
data class ListItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstText: String,
    val secondText: String
)
