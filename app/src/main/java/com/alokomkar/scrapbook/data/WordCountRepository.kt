package com.alokomkar.scrapbook.data

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData


class WordCountRepository( val application: Application ) : DataAPI, TaskAPI {


    //https://www.journaldev.com/20126/android-rss-feed-app
    //https://github.com/hasancse91/android-web-scraping-app-jsoup/blob/master/WebScrapingbyJsoup-code/app/src/main/java/com/hellohasan/webscrapingbyjsoup/Parser/HtmlParser.java
    //https://medium.com/@princessdharmy/getting-started-with-jsoup-in-android-594e89dc891f
    //https://www.yudiz.com/data-scraping-in-android-using-jsoupjava-html-parser/
    //https://www.tutorialspoint.com/jsoup/jsoup_parse_string.htm

    private var mIsFiltered : Boolean = false
    private var mUrl : String ?= null
    private val mMutableContent = MutableLiveData<List<WordCount>>()
    private val mResponseDao = SBRoomDatabase.getDbInstance(application).responseDao()
    private lateinit var mTaskAPI : TaskAPI

    override fun toggleFilter( isFiltered: Boolean ) {
        mIsFiltered = isFiltered
        if( mUrl != null )
            parseUrl(mUrl!!)
    }

    @Synchronized
    override fun parseUrl(url: String) {
        mTaskAPI  = WordCountTask.getTaskAPI( application, mResponseDao, this, mIsFiltered )
        mTaskAPI.execute( url )
        //fetchParsedData()
    }

    override fun fetchParsedData(): LiveData<List<WordCount>>
            = mMutableContent

    override fun onDataFetched(contentList: List<WordCount>?)
            = mMutableContent.postValue( contentList )

    override fun execute(url: String) {}

}