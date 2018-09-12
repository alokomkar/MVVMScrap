package com.alokomkar.scrapbook

interface TaskAPI {
    fun onDataFetched( contentList : List<WordCount>? )
    fun execute( url : String )
}