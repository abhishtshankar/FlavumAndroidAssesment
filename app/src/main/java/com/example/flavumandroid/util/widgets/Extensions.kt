package com.example.flavumandroid.util.widgets

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.flavumandroid.home.model.Day
import com.example.flavumandroid.home.model.Slot
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.*

const val DATE_FORMAT_MONTH_YEAR = "MMMM yyyy"

fun Activity.hideSoftKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.monthToArray(): ArrayList<Day> {
    val daysInMonthArray: ArrayList<Day> = ArrayList()
    val yearMonth: YearMonth = YearMonth.from(this)

    val current = this.toString().split("-")
    val daysInMonth: Int = yearMonth.lengthOfMonth()
    val firstOfMonth = this.withDayOfMonth(1)
    var dayOfWeek = firstOfMonth.dayOfWeek.value
    dayOfWeek %= 7
    var daysOfPreviousMonth: Int = this.lengthOfPreviousMonth(dayOfWeek)
    for (i in 1..42) {
        if (i in (dayOfWeek + 1)..(daysInMonth + dayOfWeek)) {
            val day = Day(
                i - dayOfWeek,
                current[1].toInt(),
                current[0].toInt(),
                true
            )
            day.isActive = !day.date.isPastDate()
            daysInMonthArray.add(day)
        } else {
            if (i > (daysInMonth + dayOfWeek)) {
                daysInMonthArray.add(
                    Day(
                        i - daysInMonth - dayOfWeek,
                        current[1].toInt().let { if (it == 12) 1 else it + 1 },
                        current[0].toInt().let { if (current[1].toInt() == 12) it + 1 else it })
                )
            } else {
                daysInMonthArray.add(
                    Day(
                        daysOfPreviousMonth++,
                        current[1].toInt().let { if (it == 1) 12 else it - 1 },
                        current[0].toInt().let { if (current[1].toInt() == 1) it - 1 else it })
                )
            }
        }
        if (i >= (daysInMonth + dayOfWeek) && i % 7 == 0) break
    }
    return daysInMonthArray
}

@RequiresApi(Build.VERSION_CODES.O)
private fun LocalDate.lengthOfPreviousMonth(dayOfWeek: Int): Int {
    var month = this.toString().split("-")[1].toInt() - 1
    if (month == 0) month = 12
    else if (month == 13) month = 1
    var daysInMonth = 0
    when (month) {
        2 -> {
            daysInMonth = if (this.isLeapYear) 29 else 28
        }

        1, 3, 5, 7, 8, 10, 12 -> {
            daysInMonth = 31
        }

        4, 6, 9, 11 -> {
            daysInMonth = 30
        }
    }
    return daysInMonth - dayOfWeek + 1
}

fun createEmptySlots(date: String): List<Slot> {
    val currentTime = SimpleDateFormat("hh:mma", Locale.getDefault()).format(Date())
    val currentHour = currentTime.substring(0, 2).toInt()
    val currentAmPm = currentTime.substring(5)

    val slots = mutableListOf<Slot>()
    Log.d("Slots", "$currentHour $currentAmPm")

    if (currentHour <= 10) {
        Log.d("Slots", "10")
        slots.add(Slot("Doctor 1", null, "10:00am", "11:00am", date, null, true))
    }
    if (currentHour <= 12) {
        Log.d("Slots", "12")
        slots.add(Slot("Doctor 2", null, "12:00pm", "01:00pm", date, null, true))
    }
    if (currentHour <= 15) {
        Log.d("Slots", "15")
        slots.add(Slot("Doctor 3", null, "03:00pm", "04:00pm", date, null, true))
    }
    if (currentHour <= 17) {
        Log.d("Slots", "17")
        slots.add(Slot("Doctor 4", null, "05:00pm", "06:00pm", date, null, true))
    }

    return slots
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.isCurrentMonth(): LocalDate {
    if (this.month != LocalDate.now().month || this.year != LocalDate.now().year) return this.withDayOfMonth(
        1
    )
    else if (this.dayOfMonth != LocalDate.now().dayOfMonth && this.month == LocalDate.now().month && this.year == LocalDate.now().year) return LocalDate.now()
    return this
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toDateObject(): Date {
    return Date.from(atStartOfDay(ZoneId.systemDefault()).toInstant())
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.isPastDate(): Boolean {
    val date = LocalDate.parse(this)
    return date.isBefore(LocalDate.now())
}

fun getMonthYearName(format: String = "MMMM yyyy", date: Date = Date()): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(date)
}

fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun ViewGroup.inflater() = LayoutInflater.from(context)

fun RecyclerView.verticalView(context: Context, stackEnd: Boolean = false) {
    layoutManager = LinearLayoutManagerWrapper(context).apply { stackFromEnd = stackEnd }
    layoutManager
}