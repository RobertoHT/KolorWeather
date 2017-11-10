package com.example.udemy.kolorweather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Response
import com.example.udemy.kolorweather.API.API_KEY
import com.example.udemy.kolorweather.API.DARK_SKY_URL
import com.example.udemy.kolorweather.API.JSONParser
import com.example.udemy.kolorweather.models.CurrentWeather
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    val jsonParser = JSONParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tempTextView.text = getString(R.string.temp_placeholder, 0)
        precipTextView.text = getString(R.string.precip_placeholder, 0)
        getWeather()
    }

    private fun getWeather() {
        val latitude = 37.8267
        val longitude = -122.4233
        val language = getString(R.string.language)
        val units = getString(R.string.units)

        val url = "$DARK_SKY_URL/$API_KEY/$latitude,$longitude?lang=$language&units=$units"

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    val responseJSON = JSONObject(response)

                    val currentWeather = jsonParser.getCurrentWeather(responseJSON)

                    buildCurrentWeatherUI(currentWeather)
                }, Response.ErrorListener {
                    displayErrorMessage()
                })
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun displayErrorMessage() {
        val snackbar = Snackbar.make(main, "NETWORK ERROR", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY?", { getWeather() })
        snackbar.show()
    }

    private fun buildCurrentWeatherUI(currentWeather: CurrentWeather) {
        val precipProbability = (currentWeather.precip * 100).toInt()
        tempTextView.text = getString(R.string.temp_placeholder, currentWeather.temp.toInt())
        precipTextView.text = getString(R.string.precip_placeholder, precipProbability)
        descriptionTextView.text = currentWeather.summary
        iconImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, currentWeather.getIconResource(), null))
    }
}
