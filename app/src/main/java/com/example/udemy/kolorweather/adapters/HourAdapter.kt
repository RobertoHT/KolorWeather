package com.example.udemy.kolorweather.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.udemy.kolorweather.R
import com.example.udemy.kolorweather.extensions.inflate
import com.example.udemy.kolorweather.models.Hour
import kotlinx.android.synthetic.main.hourly_item.view.*

/**
 * Created by Roberto on 15/11/17.
 */
class HourAdapter(val hours:ArrayList<Hour>):RecyclerView.Adapter<HourAdapter.HourViewHolder>() {
    override fun onBindViewHolder(holder: HourViewHolder, position: Int)
        = holder.bind(hours[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder
        = HourViewHolder(parent.inflate(R.layout.hourly_item))

    override fun getItemCount(): Int = hours.size

    class HourViewHolder(hourlyItemView:View):RecyclerView.ViewHolder(hourlyItemView) {
        fun bind(hour:Hour) = with (itemView) {
            hourTextView.text = hour.getFormattedTime()
            probHourTextView.text = "${hour.precip.toInt().toString()} %"
            tempHourTextView.text = "${hour.temp.toInt().toString()} C"
        }
    }
}