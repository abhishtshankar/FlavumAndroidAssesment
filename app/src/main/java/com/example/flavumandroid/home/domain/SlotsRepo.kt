package com.example.flavumandroid.home.domain

import com.example.flavumandroid.home.model.Slot
import javax.inject.Inject

class SlotsRepo @Inject constructor(private val dao: SlotsDao) {
    fun bookSlot(slot: Slot) = dao.bookSlot(slot)
    fun getSlotsByDate(date: String) = dao.getSlotsByDate(date)
    fun getSlotsByDoctorName(name: String) = dao.getSlotsByDoctorName(name)
    fun getSlotsByPatientName(name: String) = dao.getSlotsByPatientName(name)

}