package com.example.flavumandroid.home.view

import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.flavumandroid.R
import com.example.flavumandroid.base.BaseFragment
import com.example.flavumandroid.databinding.FragmentHomeBinding
import com.example.flavumandroid.home.adapter.SlotsAdapter
import com.example.flavumandroid.home.adapter.TabAdapter
import com.example.flavumandroid.home.model.Slot
import com.example.flavumandroid.home.viewmodel.HomeViewModel
import com.example.flavumandroid.home.viewmodel.SharedViewModel
import com.example.flavumandroid.util.widgets.Constants
import com.example.flavumandroid.util.widgets.DATE_FORMAT_MONTH_YEAR
import com.example.flavumandroid.util.widgets.createEmptySlots
import com.example.flavumandroid.util.widgets.getMonthYearName
import com.example.flavumandroid.util.widgets.gone
import com.example.flavumandroid.util.widgets.isCurrentMonth
import com.example.flavumandroid.util.network.ResultOf
import com.example.flavumandroid.util.widgets.show
import com.example.flavumandroid.util.widgets.showToast
import com.example.flavumandroid.util.widgets.toDateObject
import com.example.flavumandroid.util.widgets.verticalView
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val PAGE_CENTER = 1
    private var focusPage = 1
    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedDay: String? = null
    private var currentSelectedDate: String? = null

    private val slotsAdapter by lazy {
        SlotsAdapter(pref.userType == Constants.PATIENT, ::callback)
    }

    private val viewModel: HomeViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var adapter: TabAdapter

    override fun setupView() {
        with(binding.tabs) {
            addTab(newTab().setText(Constants.BOOK_SLOT))
            addTab(newTab().setText(Constants.MY_SLOTS))
            addOnTabSelectedListener(tabSelectionListener)
        }
        binding.viewPager.offscreenPageLimit = 3
        setAdapter()
        setupRecyclerView()
    }

    // Function to show loader while data is loading
    private fun showLoader() {
        binding.apply {
            progressBar.show()
            rcvSlots.gone()
        }
    }

    // Function to hide loader when data is loaded
    private fun stopLoader() {
        binding.apply {
            progressBar.gone()
            rcvSlots.show()
        }
    }

    // Function to update the title based on the selected date
    private fun updateTitle() {
        binding.title.text = getMonthYearName(DATE_FORMAT_MONTH_YEAR, selectedDate.toDateObject())
    }

    // Function to set up the RecyclerView
    private fun setupRecyclerView() {
        binding.rcvSlots.apply {
            verticalView(context)
            adapter = slotsAdapter
        }
    }

    // Function to set the adapter for the ViewPager
    private fun setAdapter() {
        updateTitle()
        adapter = TabAdapter(childFragmentManager, lifecycle)
        binding.apply {
            viewPager.adapter = adapter
            viewPager.setCurrentItem(PAGE_CENTER, false)
            viewPager.registerOnPageChangeCallback(viewPager2ClickListener)
        }
    }

    // Tab selection listener to handle tab changes
    private val tabSelectionListener by lazy {
        object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    Constants.BOOK_SLOT -> {
                        slotsAdapter.list = emptyList()
                        binding.calendar.show()
                        stopLoader()
                        binding.noSessionsBooked.gone()
                    }

                    Constants.MY_SLOTS -> {
                        binding.calendar.gone()
                        showLoader()
                        viewModel.getSlotsByUser(pref.userType!!)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        }
    }

    // ViewPager2 change callback listener to handle swiping between months
    private val viewPager2ClickListener by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                focusPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    var swipe = 0
                    if (focusPage < PAGE_CENTER) {
                        swipe = 0
                        selectedDate = selectedDate.minusMonths(1)
                        selectedDate = selectedDate.isCurrentMonth()
                        selectedDay = selectedDate.toString()
                        adapter.setCalendar(selectedDate, swipe)
                        binding.viewPager.setCurrentItem(1, false)
                        updateTitle()
                    } else if (focusPage > PAGE_CENTER) {
                        swipe = 2
                        selectedDate = selectedDate.plusMonths(1)
                        selectedDate = selectedDate.isCurrentMonth()
                        selectedDay = selectedDate.toString()
                        adapter.setCalendar(selectedDate, swipe)
                        binding.viewPager.setCurrentItem(1, false)
                        updateTitle()
                    }
                }
            }
        }
    }

    override fun bindViewEvents() {
        super.bindViewEvents()
        binding.title.setOnClickListener(onClickListener)
        binding.ivNavLeft.setOnClickListener(onClickListener)
        binding.ivNavRight.setOnClickListener(onClickListener)
    }

    // Handling click events for title and navigation buttons
    override fun onClick(view: View) {
        when (view) {
            binding.title -> {
                // Handle title click event if needed
            }

            binding.ivNavLeft -> {
                // Code for navigating to the previous month
                selectedDate = selectedDate.minusMonths(1)
                selectedDate = selectedDate.isCurrentMonth()
                selectedDay = selectedDate.toString()
                adapter.setCalendar(selectedDate, 0)
                binding.viewPager.setCurrentItem(1, false)
                updateTitle()
            }

            binding.ivNavRight -> {
                // Code for navigating to the next month
                selectedDate = selectedDate.plusMonths(1)
                selectedDate = selectedDate.isCurrentMonth()
                selectedDay = selectedDate.toString()
                adapter.setCalendar(selectedDate, 2)
                binding.viewPager.setCurrentItem(1, false)
                updateTitle()
            }
        }
    }

    override fun bindViewModel() {
        sharedViewModel.date.observe(viewLifecycleOwner) { date -> // yyyy-MM-dd
            currentSelectedDate = date
            showLoader()
            requireContext().showToast(date)
            viewModel.getSlotsByDate(date)
        }
        viewModel.slots.observe(this) {
            when (it) {
                is ResultOf.Progress -> {
                    // Handle progress state if needed
                }

                is ResultOf.Empty -> {
                    stopLoader()
                    Log.d("Slots", "empty")
                    slotsAdapter.list = createAppointmentsList()
                }

                is ResultOf.Success -> {
                    stopLoader()
                    Log.d("Slots", "${it.value}")
                    slotsAdapter.list = createAppointmentsList(it.value)
                }

                is ResultOf.Failure -> {
                    // Handle failure state if needed
                }
            }
        }
        viewModel.slotsByUser.observe(this) {
            when (it) {
                is ResultOf.Progress -> {
                    // Handle progress state if needed
                }

                is ResultOf.Empty -> {
                    stopLoader()
                    Log.d("Slots", "empty")
                    slotsAdapter.list = emptyList()
                }

                is ResultOf.Success -> {
                    stopLoader()
                    Log.d("Slots", "${it.value}")
                    slotsAdapter.list = it.value
                }

                is ResultOf.Failure -> {
                    // Handle failure state if needed
                }
            }
        }
    }

    // Function to create a list of appointments
    private fun createAppointmentsList(bookedSlots: List<Slot?>? = null): List<Slot> {
        val list: List<Slot> = createEmptySlots(viewModel.date.value!!)
        Log.d("Slots", "$list")
        if (!bookedSlots.isNullOrEmpty()) {
            for (slot in list) {
                for (bookedSlot in bookedSlots) {
                    if (bookedSlot != null && slot.startTime == bookedSlot.startTime && slot.endTime == bookedSlot.endTime) {
                        slot.isAvailable = false
                        break
                    }
                }
            }
        }
        return list
    }

    // Callback function for booking a slot
    private fun callback(slot: Slot?) {
        val editText = EditText(requireContext())
        editText.hint = "Reason for Booking"
        val layoutParams =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
                setMargins(10, 0, 10, 0)
            }
        editText.layoutParams = layoutParams

        with(AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)) {
            setCancelable(false)
            setTitle("Book Appointment")
            setMessage("Book an Appointment with ${slot?.doctorName}")
            setView(editText)
            setPositiveButton("Yes") { dialog, which ->
                slot?.apply {
                    patientName = pref.patientName
                    date = currentSelectedDate
                    isAvailable = false
                    val reason = editText.text.toString()
                    appointmentReason = reason.ifEmpty { "N/A" }
                }
                viewModel.bookSlot(slot!!)
                Log.d("Slots", "Booking a slot")
                showLoader()
                sharedViewModel.passSelectedDate(currentSelectedDate.toString())
                dialog.dismiss()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)
}
