package com.alokomkar.scrapbook

import com.google.gson.Gson

class ResponseEntityMapper() : BaseEntityMapper<CountResponse, WordCount> {

    override fun mapFromCached(type: CountResponse): ArrayList<WordCount>
            = Gson().fromJson<ResponseCache>( type.countJson, ResponseCache::class.java ).countsList

    override fun mapToCached(url: String, type: ArrayList<WordCount>): CountResponse
            = CountResponse( url, Gson().toJson(ResponseCache(type)) )

    private class ResponseCache( var countsList : ArrayList<WordCount> = ArrayList() )

}