package com.example.flavumandroid.home.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.flavumandroid.R
import com.example.flavumandroid.databinding.ItemLayoutCalendarBinding
import com.example.flavumandroid.home.model.Day
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class CalendarDaysAdapter(
    private val daysList: ArrayList<Day>,
    private val currentDate: LocalDate,
    private val clickListener: (String, Int) -> Unit
) : RecyclerView.Adapter<CalendarDaysAdapter.ViewHolder>() {

    private val bool = BooleanArray(daysList.size)
    private val itemViewList = ArrayList<ItemLayoutCalendarBinding>()
    private var curDate = LocalDate.now().toString().split("-")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val viewBinding = DataBindingUtil.inflate<ItemLayoutCalendarBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_layout_calendar,
            parent,
            false
        )
        itemViewList.add(viewBinding)
        return ViewHolder(
            viewBinding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = daysList[position]
        holder.setData(day)
    }

    override fun getItemCount(): Int = daysList.size

    inner class ViewHolder(
        private val viewBinding: ItemLayoutCalendarBinding,
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun setData(day: Day) {
            viewBinding.apply {
                tvDay.text = day.day.toString()
                if (!day.monthDate) {
                    tvDay.dateOutOfMonth()
                }
                if (day.monthDate) {
                    if (day.isActive) tvDay.activeDay()
                    else tvDay.inActiveDay()
                }
                //yyyy-MM-dd
                val today = day.date

                if (day.monthDate) {
                    rlDate.setOnClickListener {
                        if (!bool[adapterPosition] && day.isActive) {
                            boolCheck()
                            rlDate.setBackgroundResource(R.drawable.background_active_calendar_days)
                            bool[adapterPosition] = true
                            clickListener.invoke(today, adapterPosition)
                        }
                    }
                }
            }
        }

        private fun boolCheck() {
            for (i in 0 until daysList.size) {
                if (bool[i]) {
                    itemViewList[i].rlDate.setBackgroundResource(0)
                    bool[i] = false
                }
            }
        }

        private fun TextView.dateOutOfMonth() {
            setTextColor(ContextCompat.getColor(viewBinding.root.context, R.color.transparent))
        }

        private fun TextView.activeDay() {
            setTextColor(ContextCompat.getColor(viewBinding.root.context, R.color.purple_700))
        }

        private fun TextView.inActiveDay() {
            setTextColor(ContextCompat.getColor(viewBinding.root.context, R.color.light_grey))
        }
    }


}