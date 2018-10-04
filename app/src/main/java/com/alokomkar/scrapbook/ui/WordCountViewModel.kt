package com.alokomkar.scrapbook.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.alokomkar.scrapbook.data.DataAPI
import com.alokomkar.scrapbook.data.WordCount

class WordCountViewModel( application: Application, private val dataAPI: DataAPI ) : AndroidViewModel( application ), DataAPI {

    var mIsFiltered : Boolean = false
    var mCount : Int = -1

    fun loadTask( url : String )
            = parseUrl(url)

    override fun parseUrl( url: String )
            = dataAPI.parseUrl( url )

    override fun fetchParsedData(): LiveData<List<WordCount>>
            = dataAPI.fetchParsedData()

    override fun toggleFilter(isFiltered: Boolean) {
        mIsFiltered = isFiltered
        dataAPI.toggleFilter( mIsFiltered )
    }
}