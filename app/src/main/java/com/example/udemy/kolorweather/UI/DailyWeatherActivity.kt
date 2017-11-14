package com.example.udemy.kolorweather.UI

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.udemy.kolorweather.R
import com.example.udemy.kolorweather.adapters.DayAdapter
import com.example.udemy.kolorweather.models.Day
import kotlinx.android.synthetic.main.activity_daily_weather.*

class DailyWeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_weather)

        intent.let {
            val days:ArrayList<Day> = it.getParcelableArrayListExtra(MainActivity.DAILY_WEATHER)
            val baseAdapter = DayAdapter(this, days)
            dailyListView.adapter = baseAdapter
        }

        dailyListView.emptyView = emptyTextView
    }
}
