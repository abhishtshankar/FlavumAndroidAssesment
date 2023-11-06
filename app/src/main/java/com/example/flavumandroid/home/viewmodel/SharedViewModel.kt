package com.example.flavumandroid.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flavumandroid.base.BaseViewModel
import com.example.flavumandroid.home.model.DateWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : BaseViewModel() {
    private val selectedDate = MutableLiveData<String>()
    private val _appointmentDate = MutableLiveData<List<DateWrapper>>()

    val date: LiveData<String>
        //yyyy-MM-dd
        get() = selectedDate

    fun passSelectedDate(passedDate: String) {
        selectedDate.value = passedDate
    }

    val appointmentDate: LiveData<List<DateWrapper>>
        get() = _appointmentDate

    fun showAppointmentIndicator(passedDate: List<DateWrapper>) {
        _appointmentDate.value = passedDate
    }
}