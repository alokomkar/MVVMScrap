package com.alokomkar.scrapbook

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup

class WordCountTask( private val dataAPI: TaskAPI ) : AsyncTask<String?, Void, List<WordCount>>() {

    private val TAG = WordCountTask::class.java.simpleName

    interface TaskAPI {
        fun onDataFetched( contentList : List<WordCount>? )
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: List<WordCount>?) {
        super.onPostExecute(result)
        if( result != null )
            dataAPI.onDataFetched( result )
    }

    override fun doInBackground(vararg params: String?): List<WordCount> {
        val contentList = ArrayList<WordCount>()
        if( params.isNotEmpty() ) {
            val url = params[0]
            val document = Jsoup.connect( url ).get()
            val paragraphs = document.getElementsContainingText("section-desc")
            for( para in paragraphs ) {
                Log.d(TAG, "Data : ${para.data()}"  )
                Log.d(TAG, "Text : ${para.text()}"  )
                Log.d(TAG, "Own Text : ${para.ownText()}")
                Log.d(TAG, "Tag : ${para.tagName()}"  )
            }
        }
        return contentList
    }
}