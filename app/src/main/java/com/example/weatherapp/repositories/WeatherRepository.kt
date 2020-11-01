package com.example.weatherapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.weatherapp.WeatherAPI
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.remote.BaseResponse
import com.example.weatherapp.remote.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherAPI
) : BaseResponse() {

    fun getCurrentWeather(lat: String, lon: String): LiveData<Resource<CurrentWeatherResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        val response = suspend { getApiResponse { weatherApi.getCurrentWeather(lat, lon) } }.invoke()

        if (response.status == Resource.Status.SUCCESS) {
            Resource.success(response.data)
            emit(response)
        }

        response.message?.let {
            emit(Resource.error(it))
        }
    }
}