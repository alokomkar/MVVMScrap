package com.alokomkar.scrapbook.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.alokomkar.scrapbook.data.DataAPI
import com.alokomkar.scrapbook.data.WordCount
import com.alokomkar.scrapbook.data.WordCountRepository

class WordCountViewModel( application: Application ) : AndroidViewModel( application ), DataAPI {

    private val mDataAPI : DataAPI = WordCountRepository(application)
    var mIsFiltered : Boolean = false
    var mCount : Int = -1

    fun loadTask( url : String )
            = parseUrl(url)

    override fun parseUrl( url: String )
            = mDataAPI.parseUrl( url )

    override fun fetchParsedData(): LiveData<List<WordCount>>
            = mDataAPI.fetchParsedData()

    override fun toggleFilter(isFiltered: Boolean) {
        mIsFiltered = isFiltered
        mDataAPI.toggleFilter( mIsFiltered )
    }
}