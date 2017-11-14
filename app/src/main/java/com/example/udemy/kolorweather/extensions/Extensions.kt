package com.example.udemy.kolorweather.extensions

import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Roberto on 13/11/17.
 */

operator fun JSONArray.iterator(): Iterator<JSONObject>
    = (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()

fun ViewGroup.inflate(resource:Int):View
    = LayoutInflater.from(context).inflate(resource, this, false)

fun View.displaySnack(message:String, lenght:Int = Snackbar.LENGTH_LONG, func:Snackbar.() -> Unit) {
    val snackbar = Snackbar.make(this, message, lenght)
    snackbar.func()
    snackbar.show()
}

fun Snackbar.action(message:String, listener:(View) -> Unit) {
    setAction(message, listener)
}