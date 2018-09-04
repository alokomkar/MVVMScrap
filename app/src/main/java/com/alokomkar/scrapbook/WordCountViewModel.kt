package com.alokomkar.scrapbook

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class WordCountViewModel( application: Application ) : AndroidViewModel( application ), DataAPI  {

    private val mWordCountRepository = WordCountRepository( application )
    var mIsFiltered : Boolean = false
    var mCount : Int = -1

    fun loadTask( url : String )
            = parseUrl(url)

    override fun parseUrl( url: String )
            = mWordCountRepository.parseUrl( url )

    override fun fetchParsedData(): LiveData<List<WordCount>>
            = mWordCountRepository.fetchParsedData()

    override fun toggleFilter(isFiltered: Boolean) {
        mIsFiltered = isFiltered
        mWordCountRepository.toggleFilter( mIsFiltered )
    }
}