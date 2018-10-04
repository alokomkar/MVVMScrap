package com.alokomkar.scrapbook

import android.app.Application
import com.alokomkar.scrapbook.data.DataAPI
import com.alokomkar.scrapbook.data.WordCountRepository
import com.alokomkar.scrapbook.ui.ViewModelFactory

object Injection {

    private fun provideRepository( application: Application ) : DataAPI {
        return WordCountRepository( application )
    }

    fun provideViewModelFactory( application: Application ) : ViewModelFactory {
        return ViewModelFactory( application, provideRepository(application) )
    }

}