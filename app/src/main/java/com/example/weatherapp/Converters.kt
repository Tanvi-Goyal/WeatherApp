package com.example.weatherapp

import java.text.SimpleDateFormat

class Converters {

    companion object {
        fun parseTimestamp(timestamp: String): String? {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val outputFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.parse(timestamp)
            return outputFormat.format(date)
        }
    }
}