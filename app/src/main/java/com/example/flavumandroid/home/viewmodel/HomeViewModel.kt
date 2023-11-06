package com.example.flavumandroid.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flavumandroid.base.BaseViewModel
import com.example.flavumandroid.home.domain.SlotsRepo
import com.example.flavumandroid.home.model.Slot
import com.example.flavumandroid.util.widgets.Constants
import com.example.flavumandroid.util.network.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: SlotsRepo) : BaseViewModel() {

    private val _slots = MutableLiveData<ResultOf<List<Slot?>?>>()
    val slots: LiveData<ResultOf<List<Slot?>?>>
        get() = _slots

    private val _slotsByUser = MutableLiveData<ResultOf<List<Slot?>?>>()
    val slotsByUser: LiveData<ResultOf<List<Slot?>?>>
        get() = _slotsByUser

    var date = MutableLiveData<String>("")

    fun getSlotsByDate(date: String) {
        this.date.value = date
        execute {
            _slots.value = ResultOf.Progress(true)
            kotlin.runCatching {
                repo.getSlotsByDate(date)
            }.onSuccess {
                it.observeForever { result ->
                    _slots.value =
                        if (result?.isEmpty() == true) ResultOf.Empty("No Data Available")
                        else ResultOf.Success(result)
                }
            }.onFailure {
                _slots.value = ResultOf.Failure(it.message ?: "An error occurred", it)
            }
            _slots.value = ResultOf.Progress(false)
        }
    }

    fun getSlotsByUser(userType: String) {
        execute {
            _slotsByUser.value = ResultOf.Progress(true)
            kotlin.runCatching {
                if (userType == Constants.DOCTOR)
                    repo.getSlotsByDoctorName(pref.doctorName!!)
                else repo.getSlotsByPatientName(pref.patientName!!)
            }.onSuccess {
                it.observeForever { result ->
                    _slotsByUser.value =
                        if (result?.isEmpty() == true) ResultOf.Empty("No Data Available")
                        else ResultOf.Success(result)
                }
            }.onFailure {
                _slotsByUser.value = ResultOf.Failure(it.message ?: "An error occurred", it)
            }
            _slotsByUser.value = ResultOf.Progress(false)
        }
    }

    fun bookSlot(slot: Slot) {
        execute(Dispatchers.IO) {
            repo.bookSlot(slot)
        }
    }

}