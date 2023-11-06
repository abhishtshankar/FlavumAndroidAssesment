package com.example.flavumandroid.home.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flavumandroid.home.view.MonthView
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
class TabAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private var fragList: Array<MonthView> = arrayOf(MonthView(), MonthView(), MonthView())
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return fragList[0]
            1 -> return fragList[1]
            2 -> return fragList[2]
        }
        return fragList[1]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setCalendar(currentMonth: LocalDate, swipe: Int) {
        val prevMonth = currentMonth.minusMonths(1)
        val nextMonth = currentMonth.plusMonths(1)
        //update all 3 fragments
        fragList[0].updateUI(prevMonth, false)
        fragList[1].updateUI(currentMonth, true)
        fragList[2].updateUI(nextMonth, false)
    }
}