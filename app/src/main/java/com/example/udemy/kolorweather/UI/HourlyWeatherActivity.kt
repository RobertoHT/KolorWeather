package com.example.udemy.kolorweather.UI

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.udemy.kolorweather.R
import com.example.udemy.kolorweather.adapters.HourAdapter
import com.example.udemy.kolorweather.models.Hour
import kotlinx.android.synthetic.main.activity_hourly_weather.*

class HourlyWeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hourly_weather)

        hourlyRecyclerView.layoutManager = LinearLayoutManager(this)

        intent.let {
            val hours:ArrayList<Hour> = it.getParcelableArrayListExtra(MainActivity.HOURLY_WEATHER)

            if (hours.size > 0) {
                hourlyRecyclerView.visibility = View.VISIBLE
                noDataTextView.visibility = View.GONE

                hourlyRecyclerView.adapter = HourAdapter(hours)
            } else {
                hourlyRecyclerView.visibility = View.GONE
                noDataTextView.visibility = View.VISIBLE
            }
        }
    }
}
