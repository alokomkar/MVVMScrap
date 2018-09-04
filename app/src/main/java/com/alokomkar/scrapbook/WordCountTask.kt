package com.alokomkar.scrapbook

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import java.util.*
import kotlin.collections.ArrayList

@Suppress("PrivatePropertyName")
class WordCountTask(private val taskAPI: TaskAPI, private val isFiltered : Boolean ) : AsyncTask<String?, Void, List<WordCount>>() {

    private val TAG = WordCountTask::class.java.simpleName
    private val CONTENT_TAG = "content"
    val maxStale = 60 * 60 * 24 * 28

    private val mFilterKeys = arrayOf("of", "the", "a",
            "an" , "with", "he",
            "she", "it", "they",
            "them", "in", "also",
            "because", "us")

    interface TaskAPI {
        fun onDataFetched( contentList : List<WordCount>? )
    }

    override fun onPostExecute(result: List<WordCount>?) {
        super.onPostExecute(result)
        if( result != null )
            taskAPI.onDataFetched( result )
    }

    override fun doInBackground(vararg params: String?): List<WordCount> {
        val contentList = ArrayList<WordCount>()
        if( params.isNotEmpty() ) {

            val url = params[0]
            try {

                val document = Jsoup.connect( url )
                        .header("Cache-Control", "public, max-age=" + 60 * 15 ) // If internet present
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale") // if internet not present
                        .get()
                val paragraphs = document.getElementsContainingText(CONTENT_TAG)
                val hashSet = HashSet<String>()

                //Eliminate duplicate sentences
                for( para in paragraphs )
                    hashSet.add(para.text())

                val countMap = HashMap<String, Int>()
                for( key in hashSet ) {
                    Log.d(TAG, "Text : $key"  )
                    val keysArray = ArrayList(key
                            .replace("[-+.^:,;']","")
                            .replace("  ", " ")
                            .split(" "))

                    if( isFiltered )
                        keysArray.removeAll(mFilterKeys)

                    for( word in keysArray )
                        countMap[word] = if( countMap.containsKey( word ))  countMap[word]!! + 1 else 1
                }


                for( (key, value) in countMap )
                    contentList.add( WordCount(key, value) )

                contentList.sortWith(Comparator { w1, w2 -> w2.count - w1.count })
            } catch ( e: Exception ) {
                Log.d(TAG, "Error : ${e.message}" )
                e.printStackTrace()
            }


        }
        return contentList
    }
}