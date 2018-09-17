package com.alokomkar.scrapbook.base

interface BaseEntityMapper<T, V> {
    fun mapFromCached( type : T ) : ArrayList<V>
    fun mapToCached( url : String, type : ArrayList<V> ) : T
}