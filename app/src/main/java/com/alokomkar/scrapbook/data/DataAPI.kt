package com.alokomkar.scrapbook.data

import android.arch.lifecycle.LiveData
import com.alokomkar.scrapbook.base.BaseAPI
import com.alokomkar.scrapbook.data.WordCount

interface DataAPI : BaseAPI<WordCount> {
    fun parseUrl( url : String )
    fun toggleFilter( isFiltered : Boolean )
    fun fetchParsedData() : LiveData<List<WordCount>>
}