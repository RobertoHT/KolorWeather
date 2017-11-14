package com.example.udemy.kolorweather.API

import com.example.udemy.kolorweather.extensions.iterator
import com.example.udemy.kolorweather.models.CurrentWeather
import com.example.udemy.kolorweather.models.Day
import org.json.JSONObject

/**
 * Created by Roberto on 10/11/17.
 */

fun getCurrentWeather(response: JSONObject):CurrentWeather {
    val currentJson = response.getJSONObject("currently")

    with(currentJson) {
        return CurrentWeather(getString("icon"),
                getString("summary"),
                getDouble("temperature"),
                getDouble("precipProbability"))
    }
}

fun getDailyWeather(response: JSONObject):ArrayList<Day> {
    val dailyJSON = response.getJSONObject(daily)
    val dayJSONArray = dailyJSON.getJSONArray(data)

    val days = ArrayList<Day>()
    for (jsonDay in dayJSONArray) {

        val minTemp = jsonDay.getDouble(temperatureMin)
        val maxTemp = jsonDay.getDouble(temperatureMax)
        val time = jsonDay.getLong(time)

        days.add(Day(time, minTemp, maxTemp))
    }

    return days
}