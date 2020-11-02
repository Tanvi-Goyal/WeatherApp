package com.example.weatherapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.weatherapp.WeatherAPI
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.remote.BaseResponse
import com.example.weatherapp.remote.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherAPI
) : BaseResponse() {

    fun getWeatherData(lat: String, lon: String): LiveData<Resource<WeatherResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        val response = suspend { getApiResponse { weatherApi.getWeatherData(lat, lon) } }.invoke()

        if (response.status == Resource.Status.SUCCESS) {
            Resource.success(response.data)
            emit(response)
        }

        response.message?.let {
            emit(Resource.error(it))
        }
    }

    fun getWeatherByDate(lat: String, lon: String, date: String): LiveData<Resource<WeatherResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        val response = suspend { getApiResponse { weatherApi.getWeatherDataByDate(lat, lon, date) } }.invoke()

        if (response.status == Resource.Status.SUCCESS) {
            Resource.success(response.data)
            emit(response)
        }

        response.message?.let {
            emit(Resource.error(it))
        }
    }
}