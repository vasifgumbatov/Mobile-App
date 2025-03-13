package com.example.mobileapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ListItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ListItemEntity)

    @Query("SELECT * FROM list_items")
    fun getAllItems(): LiveData<List<ListItemEntity>>

    @Query("DELETE FROM list_items")
    suspend fun clearItems()
}