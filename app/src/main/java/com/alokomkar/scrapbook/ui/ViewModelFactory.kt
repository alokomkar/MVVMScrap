package com.alokomkar.scrapbook.ui

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.alokomkar.scrapbook.data.DataAPI

class ViewModelFactory( val application: Application, private val dataAPI: DataAPI ) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordCountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordCountViewModel( application, dataAPI ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}