package com.alokomkar.scrapbook

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class WordCountViewModel( application: Application ) : AndroidViewModel( application ), DataAPI  {

    private val mWordCountRepository = WordCountRepository( application )

    override fun parseUrl(url: String)
            = mWordCountRepository.parseUrl( url )

    override fun fetchParsedData(): LiveData<List<WordCount>>
            = mWordCountRepository.fetchParsedData()
}