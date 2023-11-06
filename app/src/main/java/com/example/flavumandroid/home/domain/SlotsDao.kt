package com.example.flavumandroid.home.domain


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.flavumandroid.home.model.Slot

@Dao
interface SlotsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun bookSlot(slot: Slot)

    @Query("SELECT * FROM slots WHERE date == :date ORDER BY date ASC")
    fun getSlotsByDate(date: String): LiveData<List<Slot>>

    @Query("SELECT * FROM slots WHERE doctorName == :name & isAvailable = 0 ORDER BY date ASC")
    fun getSlotsByDoctorName(name: String): LiveData<List<Slot>>

    @Query("SELECT * FROM slots WHERE patientName == :name ORDER BY date ASC")
    fun getSlotsByPatientName(name: String): LiveData<List<Slot>>

}