package com.example.weatherapp.remote

import com.example.weatherapp.model.WeatherResponse
import retrofit2.Response

abstract class BaseResponse {

    protected suspend fun getApiResponse(call: suspend () -> Response<WeatherResponse>): Resource<WeatherResponse> {
        try {
            val response = call.invoke()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error("Network Error: $message")
    }
}