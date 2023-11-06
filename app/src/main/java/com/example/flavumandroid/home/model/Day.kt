package com.example.flavumandroid.home.model

data class Day(
    val day: Int,
    val month: Int,
    val year: Int,
    val monthDate: Boolean = false,
    //yyyy MM dd
    val date: String = "$year-${if (month < 10) "0$month" else month}-${if (day < 10) "0$day" else day}",
    var isActive: Boolean = false
)