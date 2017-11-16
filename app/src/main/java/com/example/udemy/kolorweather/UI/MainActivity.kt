package com.example.udemy.kolorweather.UI

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
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
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    val TAG = MainActivity::class.java.simpleName
    var days:ArrayList<Day> = ArrayList()
    var hours:ArrayList<Hour> = ArrayList()
    val REQUEST_PERMISSION = 10
    lateinit var apiClient:GoogleApiClient
    companion object {
        val DAILY_WEATHER = "DAILY_WEATHER"
        val HOURLY_WEATHER = "HOURLY_WEATHER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tempTextView.text = getString(R.string.temp_placeholder, 0)
        precipTextView.text = getString(R.string.precip_placeholder, 0)

        apiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build()
    }

    override fun onConnected(p0: Bundle?) {
        checkPermission()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onConnectionSuspended(p0: Int) {}

    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED) {

            val lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient)
            getWeather(lastLocation)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient)
            getWeather(lastLocation)
        } else {
            Snackbar.make(main, getString(R.string.error_permission), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun getWeather(location:Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        val language = getString(R.string.language)
        val units = getString(R.string.units)

        val geo = Geocoder(this, Locale.getDefault())
        var city = ""
        val address:ArrayList<Address> = geo.getFromLocation(latitude, longitude, 1) as ArrayList<Address>
        if (address.size > 0) {
            Log.d(TAG, "Ciudad: ${address.get(0).locality}, ${address.get(0).subLocality} : $latitude, $longitude")
            city = address.get(0).locality
        }

        val url = "$DARK_SKY_URL/$API_KEY/$latitude,$longitude?lang=$language&units=$units"

        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> {
                    val responseJSON = JSONObject(it)

                    val currentWeather = getCurrentWeather(responseJSON)

                    days = getDailyWeather(responseJSON)
                    hours = getHourlyWeather(responseJSON)

                    buildCurrentWeatherUI(currentWeather, city)
                }, Response.ErrorListener {
                    displayErrorMessage()
                })
        queue.add(stringRequest)
    }

    private fun displayErrorMessage() {
        main.displaySnack(getString(R.string.network_error), Snackbar.LENGTH_INDEFINITE) {
            action(getString(R.string.retry)) {
                checkPermission()
            }
        }
    }

    private fun buildCurrentWeatherUI(currentWeather: CurrentWeather, city:String) {
        val precipProbability = (currentWeather.precip * 100).toInt()
        localizationTextView.text = city
        hourTextView.text = currentWeather.getFormattedHour()
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
