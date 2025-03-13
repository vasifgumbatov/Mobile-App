package com.example.mobileapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.local.AppDatabase
import com.example.mobileapp.data.local.ListItemEntity
import com.example.mobileapp.data.preferences.AppPreferences
import com.example.mobileapp.domain.ListItemRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ListItemRepository
    private val preferences: AppPreferences

    val allItems: LiveData<List<ListItemEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).listItemDao()
        repository = ListItemRepository(dao)
        preferences = AppPreferences(application)
        allItems = repository.allItems
    }

    fun insertItem(firstText: String, secondText: String) {
        viewModelScope.launch {
            repository.insertItem(ListItemEntity(firstText = firstText, secondText = secondText))
        }
    }

    fun clearItems() {
        viewModelScope.launch {
            repository.clearItems()
        }
    }

    fun incrementPauseCount() {
        preferences.incrementPauseCount()
    }

    fun getPauseCount(): Int {
        return preferences.getPauseCount()
    }
}
