package com.alokomkar.scrapbook

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import java.util.*
import kotlin.collections.ArrayList

@Suppress("PrivatePropertyName", "DEPRECATION")
class WordCountTask(private val application : Application,
                    private val responseDao: ResponseDao,
                    private val taskAPI: TaskAPI,
                    private val isFiltered : Boolean ) : AsyncTask<String?, Void, List<WordCount>>(), TaskAPI {



    private val TAG = WordCountTask::class.java.simpleName
    private val CONTENT_TAG = "content"

    private val mFilterKeys = arrayOf("of", "the", "a",
            "an" , "with", "he",
            "she", "it", "they",
            "them", "in", "also",
            "because", "us")



    override fun onPostExecute(result: List<WordCount>?) {
        super.onPostExecute(result)
        if( result != null )
            onDataFetched( result )
    }

    override fun doInBackground(vararg params: String?): List<WordCount> {
        val contentList = ArrayList<WordCount>()
        if( params.isNotEmpty() ) {

            val url = params[0]
            try {

                if( isNetworkAvailable() ) {
                    //TODO : Not working as intended - needs a different approach - Low priority
                    val document =
                            Jsoup.connect( url )
                                    .header("Cache-Control", "public, max-age=" + 60 * 15 ) // If internet present
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

                    val cacheResponse = ResponseEntityMapper().mapToCached( params[0]!!,
                            ArrayList(contentList.subList(0, if( contentList.size > 5 ) 6 else contentList.size ) ) )
                    responseDao.insertOrUpdate( cacheResponse )
                }
                else {
                    val cachedResponse = responseDao.findById(params[0]!!)
                    if( cachedResponse != null ) {
                        contentList.addAll( ResponseEntityMapper().mapFromCached(cachedResponse) )
                    }
                    if( isFiltered ) {
                        val filteredList = ArrayList( contentList.filterNot { mFilterKeys.contains( it.word ) } )
                        contentList.clear()
                        contentList.addAll(filteredList)
                    }
                    contentList.sortWith(Comparator { w1, w2 -> w2.count - w1.count })

                }
            } catch ( e: Exception ) {
                Log.d(TAG, "Error : ${e.message}" )
                e.printStackTrace()
            }


        }
        return contentList
    }

    private fun isNetworkAvailable() : Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    override fun onDataFetched(contentList: List<WordCount>?) {
        taskAPI.onDataFetched( contentList )
    }

    override fun execute(url: String) {
        //To check communication : taskAPI.onDataFetched( ArrayList() )
        this.executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR, url )
    }

    companion object {

        fun getTaskAPI(application: Application, responseDao: ResponseDao, taskAPI: TaskAPI, mIsFiltered: Boolean): TaskAPI {
            return WordCountTask( application, responseDao, taskAPI, mIsFiltered )
        }

    }
}