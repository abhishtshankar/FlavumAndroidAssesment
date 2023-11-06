package com.example.flavumandroid.home.view

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flavumandroid.base.BaseFragment
import com.example.flavumandroid.databinding.FragmentMonthViewBinding
import com.example.flavumandroid.home.adapter.CalendarDaysAdapter
import com.example.flavumandroid.home.model.Day
import com.example.flavumandroid.home.viewmodel.SharedViewModel
import com.example.flavumandroid.util.widgets.monthToArray
import java.time.LocalDate
import java.util.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class MonthView : BaseFragment<FragmentMonthViewBinding>() {

    // List of days in the current month
    private lateinit var daysInMonthList: ArrayList<Day>
    private lateinit var calendarAdapter: CalendarDaysAdapter

    // Shared ViewModel for communication with the parent activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // Current date displayed in the calendar
    private var currentDate: LocalDate = LocalDate.now()

    companion object {
        private var currentDay: String? = null
    }

    // Handle click events (not implemented in this class)
    override fun onClick(view: View) {
        // Handle click events if necessary
    }

    // Inflate the view binding for the fragment
    override fun getViewBinding() = FragmentMonthViewBinding.inflate(layoutInflater)

    // Initialize the view and set up the calendar
    override fun setupView() {
        currentDay = currentDate.toString()
        daysInMonthList = currentDate.monthToArray()
        subscribeToObservers(currentDate)
        setRecyclerView()
    }

    // Bind ViewModel (not implemented in this class)
    override fun bindViewModel() {
        // Bind ViewModel if needed
    }

    // Subscribe to observers for changes in appointment dates
    private fun subscribeToObservers(date: LocalDate) {
        sharedViewModel.appointmentDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            // Calculate the extra days at the beginning of the month (to adjust the highlighting)
            var extra = currentDate.withDayOfMonth(1).dayOfWeek.value
            extra %= 7
            // Highlight the selected date in the calendar (not implemented here)
            // calendarAdapter.highLight(it, extra)
        })
    }

    // Update the UI when the displayed month changes
    fun updateUI(date: LocalDate, currentMonth: Boolean) {
        currentDate = date
        currentDay = currentDate.toString()
        if (currentMonth) subscribeToObservers(date)
        setRecyclerView()
    }

    // Set up the RecyclerView to display the calendar days
    private fun setRecyclerView() {
        daysInMonthList = ArrayList()
        daysInMonthList = currentDate.monthToArray()
        calendarAdapter = CalendarDaysAdapter(
            daysInMonthList,
            currentDate,
            ::onDateClicked
        )

        val gridLayoutManager = GridLayoutManager(requireContext(), 7)
        binding.calendarRecyclerView.apply {
            adapter = calendarAdapter
            layoutManager = gridLayoutManager
        }
    }

    // Handle click on a date in the calendar
    private fun onDateClicked(today: String, position: Int) {
        currentDay = today
        sharedViewModel.passSelectedDate(today)
    }
}
