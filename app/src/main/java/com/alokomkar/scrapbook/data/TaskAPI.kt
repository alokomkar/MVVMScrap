package com.alokomkar.scrapbook.data

import com.alokomkar.scrapbook.data.WordCount

interface TaskAPI {
    fun onDataFetched( contentList : List<WordCount>? )
    fun execute( url : String )
}