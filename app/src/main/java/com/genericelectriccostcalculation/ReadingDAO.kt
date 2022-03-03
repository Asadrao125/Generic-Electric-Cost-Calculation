package com.genericelectriccostcalculation

import androidx.room.*

@Dao
interface ReadingDAO {
    @Insert
    suspend fun insertReading(reading: Reading)

    @Update
    suspend fun updateReading(reading: Reading)

    @Delete
    suspend fun deleteReading(reading: Reading)

    @Query("SELECT * FROM meter_reading ORDER BY id DESC")
    fun getAllReadings(): List<Reading>

    @Query("SELECT EXISTS(SELECT serviceNumber FROM meter_reading WHERE serviceNumber = :serviceNumber)")
    fun checkExist(serviceNumber: String): Boolean
}