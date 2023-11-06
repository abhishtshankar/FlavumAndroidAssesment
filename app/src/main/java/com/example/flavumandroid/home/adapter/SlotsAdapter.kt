package com.example.flavumandroid.home.adapter

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.flavumandroid.R
import com.example.flavumandroid.databinding.ItemLayoutSlotBinding
import com.example.flavumandroid.home.model.Slot
import com.example.flavumandroid.util.widgets.gone
import com.example.flavumandroid.util.widgets.inflater
import com.example.flavumandroid.util.widgets.show

@RequiresApi(Build.VERSION_CODES.O)
class SlotsAdapter(val isPatient: Boolean?, val job: (Slot?) -> Unit) :
    RecyclerView.Adapter<SlotsAdapter.VH>() {

    var list: List<Slot?>? = null
        set(value) {
            field = value
            notifyItemRangeInserted(0, list?.size ?: 0)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemLayoutSlotBinding.inflate(parent.inflater()))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.setData(list?.get(position))
    }

    override fun getItemCount(): Int = list?.size ?: 0

    inner class VH(private val binding: ItemLayoutSlotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(slot: Slot?) {
            binding.apply {
                date.text = slot?.date
                appointmentReason.text = slot?.appointmentReason
                doctorName.text = slot?.doctorName
                sessionTiming.text = binding.root.context.resources.getString(
                    R.string.session_timing,
                    slot?.startTime,
                    slot?.endTime
                )
                var buttonColor = R.color.green
                val buttonText =
                    if (isPatient == true) {
                        if (slot?.isAvailable == true) {
                            buttonColor = R.color.green
                            R.string.book
                        } else {
                            buttonColor = R.color.light_grey
                            R.string.booked
                        }
                    } else {
                        if (slot?.isAvailable == true) {
                            buttonColor = R.color.green
                            R.string.free
                        } else {
                            buttonColor = R.color.purple_200
                            R.string.pending
                        }
                    }

                bookSession.text = root.context.getString(buttonText)
                bookSession.setBackgroundColor(
                    ContextCompat.getColor(root.context, buttonColor)
                )
                if (slot?.appointmentReason == null) {
                    appointmentReasonTitle.gone()
                    appointmentReason.gone()
                } else {
                    appointmentReasonTitle.show()
                    appointmentReason.show()
                }
                if (slot?.isAvailable == true) {
                    patientName.gone()
                    patientNameTitle.gone()
                    if (isPatient == true) {
                        bookSession.setOnClickListener {
                            job.invoke(slot)
                        }
                    }
                } else {
                    patientName.show()
                    patientNameTitle.show()
                    patientName.text = slot?.patientName
                }
            }
        }
    }
}