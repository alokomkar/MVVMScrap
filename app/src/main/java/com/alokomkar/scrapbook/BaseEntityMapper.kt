package com.alokomkar.scrapbook

interface BaseEntityMapper<T, V> {
    fun mapFromCached( type : T ) : ArrayList<V>
    fun mapToCached( url : String, type : ArrayList<V> ) : T
}