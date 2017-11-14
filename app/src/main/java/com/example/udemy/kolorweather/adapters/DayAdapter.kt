package com.example.udemy.kolorweather.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.udemy.kolorweather.R
import com.example.udemy.kolorweather.extensions.inflate
import com.example.udemy.kolorweather.models.Day

/**
 * Created by Roberto on 14/11/17.
 */

class DayAdapter(val context:Context, val datasource:ArrayList<Day>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder:ViewHolder
        val view:View

        if (convertView == null) {
            view = parent.inflate(R.layout.daily_item)
            viewHolder = ViewHolder(view)
            view.tag= viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }

        val currentDay = datasource[position]

        viewHolder.apply {
            dayTextView.text = currentDay.getFormattedTime()
            minTextView.text = "Min ${currentDay.minTemp.toInt()} C"
            maxTextView.text = "Max ${currentDay.maxTemp.toInt()} C"
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return datasource[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return datasource.size
    }

    private class ViewHolder(view:View) {
        val dayTextView:TextView = view.findViewById(R.id.dayTextView)
        val minTextView:TextView = view.findViewById(R.id.minTextView)
        val maxTextView:TextView = view.findViewById(R.id.maxTextView)
    }
}