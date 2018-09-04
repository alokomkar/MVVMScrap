package com.alokomkar.scrapbook

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

class WordCountRepository( application: Application ) : DataAPI {

    override fun parseUrl(url: String) {
        WordCountTask(this ).executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR )
    }

    override fun fetchParsedData(): LiveData<List<WordCount>> {

    }
}