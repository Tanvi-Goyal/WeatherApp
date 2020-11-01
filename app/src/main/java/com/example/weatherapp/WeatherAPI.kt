package com.example.weatherapp

import com.example.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET(ApiConstants.WEATHER_URL)
    suspend fun getWeatherData(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Response<WeatherResponse>
}