package com.example.udemy.kolorweather.API

import com.example.udemy.kolorweather.models.CurrentWeather
import org.json.JSONObject

/**
 * Created by Roberto on 10/11/17.
 */
class JSONParser {
    fun getCurrentWeather(response: JSONObject):CurrentWeather {
        val currentJson = response.getJSONObject("currently")

        with(currentJson) {
            return CurrentWeather(getString("icon"),
                    getString("summary"),
                    getDouble("temperature"),
                    getDouble("precipProbability"))
        }
    }
}