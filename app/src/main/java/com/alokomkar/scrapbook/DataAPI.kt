package com.alokomkar.scrapbook

import android.arch.lifecycle.LiveData

interface DataAPI : BaseAPI<WordCount> {
    fun parseUrl( url : String )
    fun toggleFilter( isFiltered : Boolean )
    fun fetchParsedData() : LiveData<List<WordCount>>
}