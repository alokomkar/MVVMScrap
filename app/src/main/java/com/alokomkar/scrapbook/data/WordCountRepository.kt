package com.alokomkar.scrapbook.data

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder


class WordCountRepository(private val application: Application ) : DataAPI, TaskAPI, ServiceConnection, WordCountService.WordResultReceiver {

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        if( resultCode == Activity.RESULT_OK && resultData != null ) {
            onDataFetched( resultData.getParcelableArrayList(WordCount::class.java.simpleName) )
        }
    }


    //https://www.journaldev.com/20126/android-rss-feed-app
    //https://github.com/hasancse91/android-web-scraping-app-jsoup/blob/master/WebScrapingbyJsoup-code/app/src/main/java/com/hellohasan/webscrapingbyjsoup/Parser/HtmlParser.java
    //https://medium.com/@princessdharmy/getting-started-with-jsoup-in-android-594e89dc891f
    //https://www.yudiz.com/data-scraping-in-android-using-jsoupjava-html-parser/
    //https://www.tutorialspoint.com/jsoup/jsoup_parse_string.htm

    private var mIsFiltered : Boolean = false
    private var mUrl : String ?= null
    private val mMutableContent = MutableLiveData<List<WordCount>>()
    private var mService: WordCountService ?= null
    private val mResultReceiver : WordCountService.WordCountResultReceiver = WordCountService.WordCountResultReceiver( Handler() )

    override fun toggleFilter( isFiltered: Boolean ) {
        mIsFiltered = isFiltered
        if( mUrl != null )
            parseUrl(mUrl!!)
    }

    @Synchronized
    override fun parseUrl(url: String) {
        mResultReceiver.setResultReceiver( this )
        WordCountService.startService( application, url, mIsFiltered, mResultReceiver )
        //fetchParsedData()
    }

    override fun fetchParsedData(): LiveData<List<WordCount>>
            = mMutableContent

    override fun onDataFetched(contentList: List<WordCount>?)
            = mMutableContent.postValue( contentList )

    override fun execute(url: String) {}

    override fun onServiceDisconnected( componentName : ComponentName?) {
        mService = null
    }

    override fun onServiceConnected( componentName: ComponentName?, binder: IBinder?) {
        mService = ( binder as WordCountService.WordCountBinder ).getService()
    }

}