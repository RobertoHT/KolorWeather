package com.example.udemy.kolorweather.UI

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Response
import com.example.udemy.kolorweather.API.*
import com.example.udemy.kolorweather.R
import com.example.udemy.kolorweather.extensions.action
import com.example.udemy.kolorweather.extensions.displaySnack
import com.example.udemy.kolorweather.models.CurrentWeather
import com.example.udemy.kolorweather.models.Day
import com.example.udemy.kolorweather.models.Hour
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    var days:ArrayList<Day> = ArrayList()
    var hours:ArrayList<Hour> = ArrayList()
    companion object {
        val DAILY_WEATHER = "DAILY_WEATHER"
        val HOURLY_WEATHER = "HOURLY_WEATHER"
    }

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
                Response.Listener<String> {
                    val responseJSON = JSONObject(it)

                    val currentWeather = getCurrentWeather(responseJSON)

                    days = getDailyWeather(responseJSON)
                    hours = getHourlyWeather(responseJSON)

                    buildCurrentWeatherUI(currentWeather)
                }, Response.ErrorListener {
                    displayErrorMessage()
                })
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun displayErrorMessage() {
        main.displaySnack(getString(R.string.network_error), Snackbar.LENGTH_INDEFINITE) {
            action(getString(R.string.retry)) {
                getWeather()
            }
        }
    }

    private fun buildCurrentWeatherUI(currentWeather: CurrentWeather) {
        val precipProbability = (currentWeather.precip * 100).toInt()
        tempTextView.text = getString(R.string.temp_placeholder, currentWeather.temp.toInt())
        precipTextView.text = getString(R.string.precip_placeholder, precipProbability)
        descriptionTextView.text = currentWeather.summary
        iconImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, currentWeather.getIconResource(), null))
    }

    fun startDailyActivity(view:View) {
        val intent = Intent(this, DailyWeatherActivity::class.java).apply {
            putParcelableArrayListExtra(DAILY_WEATHER, days)
        }
        startActivity(intent)
    }

    fun startHourlyActivity(view:View) {
        val intent = Intent(this, HourlyWeatherActivity::class.java).apply {
            putParcelableArrayListExtra(HOURLY_WEATHER, hours)
        }
        startActivity(intent)
    }
}
