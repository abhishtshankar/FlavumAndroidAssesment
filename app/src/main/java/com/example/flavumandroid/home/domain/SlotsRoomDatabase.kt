package com.example.flavumandroid.home.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flavumandroid.home.model.Slot

@Database(entities = [Slot::class], version = 1, exportSchema = false)
abstract class SlotsRoomDatabase : RoomDatabase() {

    abstract fun getDao(): SlotsDao

}