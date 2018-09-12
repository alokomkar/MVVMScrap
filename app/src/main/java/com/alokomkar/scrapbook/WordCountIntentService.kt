package com.alokomkar.scrapbook

import android.app.Application
import android.app.IntentService
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import org.jsoup.Jsoup
import java.util.HashMap
import java.util.HashSet

@Suppress("PrivatePropertyName")
class WordCountIntentService : IntentService( WordCountIntentService::class.java.simpleName ) {

    companion object {

        private const val URL = "url"
        private const val FLAG_FILTERED = "isFiltered"

        fun parseUrl( url : String, isFiltered : Boolean, application: Application ) {
            val intent = Intent( application, WordCountIntentService::class.java )
            intent.putExtra(URL, url)
            intent.putExtra(FLAG_FILTERED, isFiltered)
            application.startService( intent )
        }
    }

    private val TAG = WordCountTask::class.java.simpleName
    private val CONTENT_TAG = "content"
    private val maxStale = 60 * 60 * 24 * 28
    private val mMutableContent = MutableLiveData<List<WordCount>>()

    private val mFilterKeys = arrayOf("of", "the", "a",
            "an" , "with", "he",
            "she", "it", "they",
            "them", "in", "also",
            "because", "us")

    @Suppress("DEPRECATION")
    private fun isNetworkAvailable() : Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }


    override fun onHandleIntent(intent: Intent?) {

        if( intent != null ) {
            val contentList = ArrayList<WordCount>()

            val url = intent.getStringExtra(URL)
            try {

                //TODO : Not working as intended - needs a different approach - Low priority
                val document = if( isNetworkAvailable() )
                    Jsoup.connect( url )
                            .header("Cache-Control", "public, max-age=" + 60 * 15 ) // If internet present
                            .get()
                else
                    Jsoup.connect( url )
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

                    if( intent.getBooleanExtra( FLAG_FILTERED, false ) )
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

            mMutableContent.postValue( contentList )
        }

    }


}