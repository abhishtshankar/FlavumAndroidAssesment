package com.example.flavumandroid.util.widgets

import com.example.flavumandroid.login.model.UserType

object Constants {
    val DOCTOR = "Doctor"
    val PATIENT = "Patient"
    val BOOK_SLOT = "Book Your Slot"
    val MY_SLOTS = "My Booked Slots"
}

val users by lazy {
    arrayOf(
        UserType(Constants.DOCTOR, "doctor 1"),
        UserType(Constants.DOCTOR, "doctor 2"),
        UserType(Constants.PATIENT, "patient 1"),
        UserType(Constants.PATIENT, "patient 2"),
    )
}

