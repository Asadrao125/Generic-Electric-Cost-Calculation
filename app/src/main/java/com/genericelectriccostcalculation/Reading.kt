package com.genericelectriccostcalculation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meter_reading")
data class Reading(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var serviceNumber: String,
    var date: String,
    var billAmount: String,
    var unitsConsume: String
)