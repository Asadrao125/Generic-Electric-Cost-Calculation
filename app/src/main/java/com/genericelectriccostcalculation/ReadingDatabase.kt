package com.genericelectriccostcalculation

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Reading::class], version = 1)
abstract class ReadingDatabase : RoomDatabase() {

    abstract fun readingDao(): ReadingDAO

}