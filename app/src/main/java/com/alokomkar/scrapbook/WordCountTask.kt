package com.alokomkar.scrapbook

import android.os.AsyncTask

class WordCountTask( private val dataAPI: DataAPI ) : AsyncTask<String?, Void, List<WordCount>>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: List<WordCount>?) {
        super.onPostExecute(result)
    }

    override fun doInBackground(vararg url: String?): List<WordCount> {
        val contentList = ArrayList<WordCount>()

        return contentList
    }
}