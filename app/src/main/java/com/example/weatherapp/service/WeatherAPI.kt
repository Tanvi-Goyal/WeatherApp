package com.example.weatherapp.service

import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.utils.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET(ApiConstants.WEATHER_URL)
    suspend fun getWeatherData(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Response<WeatherResponse>

    @GET(ApiConstants.WEATHER_DATE_URL)
    suspend fun getWeatherDataByDate(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("dt") date: String
    ): Response<WeatherResponse>
}