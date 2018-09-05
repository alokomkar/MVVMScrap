package com.alokomkar.scrapbook

import android.app.Application
import android.net.http.HttpResponseCache
import android.util.Log
import java.io.File
import java.io.IOException


class SBApplication : Application() {

    private val TAG = SBApplication::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        try {
            val httpCacheDir = File( this.cacheDir, "http" )
            val httpCacheSize = (10 * 1024 * 1024).toLong() // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize)
        } catch (e: IOException) {
            Log.i(TAG, "HTTP response cache installation failed:$e")
        }

    }
}