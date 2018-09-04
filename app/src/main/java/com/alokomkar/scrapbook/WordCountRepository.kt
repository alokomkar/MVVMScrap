package com.alokomkar.scrapbook

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

class WordCountRepository( application: Application ) : DataAPI {

    //https://www.journaldev.com/20126/android-rss-feed-app
    //jsoup parsing mvvm android
    //https://github.com/hasancse91/android-web-scraping-app-jsoup/blob/master/WebScrapingbyJsoup-code/app/src/main/java/com/hellohasan/webscrapingbyjsoup/Parser/HtmlParser.java
    //https://medium.com/@princessdharmy/getting-started-with-jsoup-in-android-594e89dc891f
    //https://www.yudiz.com/data-scraping-in-android-using-jsoupjava-html-parser/
    override fun parseUrl(url: String) {
        WordCountTask(this ).executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR )
    }

    override fun fetchParsedData(): LiveData<List<WordCount>> {

    }
}