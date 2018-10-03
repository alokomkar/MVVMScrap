package com.alokomkar.scrapbook.data

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*

//Using a Service won't ensure smooth user experience - all tasks need to have a thread / async task / others spawned to ensure smooth operation
//https://medium.com/@ankit_aggarwal/ways-to-communicate-between-activity-and-service-6a8f07275297
//https://proandroiddev.com/deep-dive-into-android-services-4830b8c9a09
class WordCountService : Service(), TaskAPI {

    private var mContentList : List<WordCount> ?= null

    //Use this when Service is not waiting on a long running task
    private val mWordCountBinder = WordCountBinder()

    //For long running tasks - communication via result receiver
    private var mResultReceiver : ResultReceiver ?= null

    //http://www.vogella.com/tutorials/AndroidServices/article.html#exercise_bindlocalservice
    override fun onDataFetched(contentList: List<WordCount>?) {
        //Send back this data to UI with IBinder : Service - public method
        this.mContentList = contentList
        if( mResultReceiver != null ) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(WordCount::class.java.simpleName, if( contentList != null ) ArrayList(mContentList) else ArrayList() )
            this.mResultReceiver?.send( Activity.RESULT_OK, bundle )
        }
        stopSelf()
    }

    override fun execute(url: String) {}

    override fun onBind(intent: Intent?): IBinder? {
        return mWordCountBinder
    }

    inner class WordCountBinder : Binder() {
        fun getService() : WordCountService = this@WordCountService
    }

    interface WordResultReceiver {
        fun onReceiveResult(resultCode: Int, resultData: Bundle?)
    }

    //To communicate with main screen - activity or fragment - https://stackoverflow.com/questions/4510974/using-resultreceiver-in-android
    class WordCountResultReceiver( handler : Handler ) : ResultReceiver( handler ) {

        private var mWordResultReceiver : WordResultReceiver ?= null

        fun setResultReceiver( wordResultReceiver: WordResultReceiver ) {
            this.mWordResultReceiver = wordResultReceiver
        }

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            if( mWordResultReceiver != null )
                mWordResultReceiver?.onReceiveResult( resultCode, resultData )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if( intent != null ) {
            val url = intent.getStringExtra(keyUrl)
            val mResponseDao = SBRoomDatabase.getDbInstance(application).responseDao()
            val mTaskAPI  = WordCountTask.getTaskAPI( application, mResponseDao, this, intent.getBooleanExtra(isFiltered, false) )
            mResultReceiver = intent.getParcelableExtra(resultReceiver)
            mTaskAPI.execute( url )
        }
        return Service.START_REDELIVER_INTENT
    }

    companion object {

        private const val keyUrl = "keyUrl"
        private const val isFiltered = "isFiltered"
        private const val resultReceiver = "resultReceiver"

        fun startService(context : Context, url : String, filtered : Boolean, wordCountResultReceiver: WordCountResultReceiver ) {
            val intent = Intent( context, WordCountService::class.java )
            intent.putExtra( keyUrl, url )
            intent.putExtra( isFiltered, filtered )
            intent.putExtra( resultReceiver, wordCountResultReceiver )
            context.startService( intent )
        }
    }
}