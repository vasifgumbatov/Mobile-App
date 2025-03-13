package com.example.mobileapp.domain

import androidx.lifecycle.LiveData
import com.example.mobileapp.data.local.ListItemDao
import com.example.mobileapp.data.local.ListItemEntity

class ListItemRepository(private val listItemDao: ListItemDao) {
    val allItems: LiveData<List<ListItemEntity>> = listItemDao.getAllItems()

    suspend fun insertItem(item: ListItemEntity) {
        listItemDao.insertItem(item)
    }

    suspend fun clearItems() {
        listItemDao.clearItems()
    }
}
