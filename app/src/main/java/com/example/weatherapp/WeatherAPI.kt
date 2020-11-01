package com.example.weatherapp

import com.example.weatherapp.model.CurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET(ApiConstants.CURRENT_WEATHER_URL)
    suspend fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ) : Response<CurrentWeatherResponse>
}