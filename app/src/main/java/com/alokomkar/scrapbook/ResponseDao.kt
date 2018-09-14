package com.alokomkar.scrapbook

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.util.Log

interface ResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert( masterContent: CountResponse) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update( masterContent: CountResponse)

    @Query("DELETE FROM CountResponse")
    abstract fun deleteAll()

    @Query("SELECT * FROM CountResponse ORDER BY url ASC")
    abstract fun listAllLive() : LiveData<List<CountResponse>>

    @Query("SELECT * FROM CountResponse WHERE url = :url ORDER BY url ASC")
    abstract fun listAllLiveByID( url : String ) : LiveData<List<CountResponse>>

    @Query("SELECT * FROM CountResponse WHERE url = :url")
    abstract fun findLiveById( url: String ) : LiveData<CountResponse>

    @Query("SELECT * FROM CountResponse WHERE url = :url")
    abstract fun findById( url: String ) : CountResponse?

    @Transaction
    open fun insertOrUpdate(countResponse: CountResponse) {
        Log.d( ResponseDao::class.java.simpleName, "insertOrUpdate : attempt insert : $countResponse")
        val id = insert( countResponse )
        if( id == -1L ) {
            Log.d( ResponseDao::class.java.simpleName, "insertOrUpdate : attempt update : $countResponse")
            update( countResponse )
        }
    }
    
}