package com.alokomkar.scrapbook.data

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase

@Database(entities = [CountResponse::class], version = 1)
abstract class SBRoomDatabase : RoomDatabase() {

    abstract fun responseDao() : ResponseDao

    companion object {

        private var dbInstance : SBRoomDatabase?= null

        fun getDbInstance( application: Application) : SBRoomDatabase {
            if (dbInstance == null) {
                synchronized(SBRoomDatabase::class.java) {
                    if (dbInstance == null) {
                        dbInstance = Room.databaseBuilder(application,
                                SBRoomDatabase::class.java, "sbRoomDatabase")
                                .build()

                    }
                }
            }
            return dbInstance!!
        }
    }
}