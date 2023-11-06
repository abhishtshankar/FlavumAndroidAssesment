package com.example.flavumandroid.home.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "slots")
data class Slot(
    @ColumnInfo(name = "doctorName") var doctorName: String? = null,
    @ColumnInfo(name = "patientName") var patientName: String? = null,
    @ColumnInfo(name = "startTime") var startTime: String? = null,
    @ColumnInfo(name = "endTime") var endTime: String? = null,
    @ColumnInfo(name = "date") var date: String? = null,
    @ColumnInfo(name = "appointmentReason") var appointmentReason: String? = null,
    @ColumnInfo(name = "isAvailable") var isAvailable: Boolean? = null
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}