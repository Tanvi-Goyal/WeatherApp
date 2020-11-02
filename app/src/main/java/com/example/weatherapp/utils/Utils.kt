package com.example.weatherapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapp.model.LatLong
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Utils {

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun getConvertedDate(timeStamp: Long): LocalDateTime {
            val secondApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val timestampAsDateString = DateTimeFormatter.ISO_INSTANT
                .format(java.time.Instant.ofEpochSecond(timeStamp))

            return LocalDateTime.parse(timestampAsDateString, secondApiFormat)
        }

        fun getLatLongByCityName(city: String): LatLong {
            return when (city) {
                "Delhi" -> {
                    LatLong("28.70", "77.10")
                }
                "Mumbai" -> {
                    LatLong("19.07", "72.87")
                }
                "Noida" -> {
                    LatLong("28.53", "77.39")
                }
                else -> LatLong("", "")
            }
        }
    }
}