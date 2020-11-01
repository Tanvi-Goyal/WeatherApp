package com.example.weatherapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.weatherapp.repositories.WeatherRepository

class WeatherViewModel @ViewModelInject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    fun getWeatherData(lat: String, lon: String) =
        repository.getWeatherData(lat, lon)
}
