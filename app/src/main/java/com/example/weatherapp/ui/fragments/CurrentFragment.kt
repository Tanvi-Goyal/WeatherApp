package com.example.weatherapp.ui.fragments

import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.FragmentCurrentBinding
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.remote.Resource
import com.example.weatherapp.utils.Utils.Companion.getConvertedDate
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class CurrentFragment : Fragment() {

    private lateinit var binding: FragmentCurrentBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var data: WeatherResponse

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModelObservers()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {

        binding.layout.visibility = View.VISIBLE
        if (::data.isInitialized) {
            val current = data.current

            val addresses: List<Address>
            val geocoder = Geocoder(requireContext(), Locale.getDefault())

            addresses = geocoder.getFromLocation(
                preferences.getString("latitude", "").toString().toDouble(),
                preferences.getString("longitude", "").toString().toDouble(),
                1
            )

            val address: String = addresses[0]
                .getAddressLine(0)

            val city: String = addresses[0].locality
            val state: String = addresses[0].adminArea

            binding.locationName.text = state

            val date = getConvertedDate(current.dt.toLong())
            binding.date.text =
                date.dayOfWeek.toString() + ", " + date.dayOfMonth + " " + date.month + ", " + date.year

            binding.textTemperature.text =
                if (preferences.getString("temperature_type", "").equals("fahrenheit")) {
                    ((9 / 5) * ((current.temp.minus(273.15F)
                        .roundToInt()).plus(32))).toString() + " \u2109"
                } else {
                    current.temp.minus(273.15F).roundToInt().toString() + " \u2103"
                }

            binding.weatherDescription.text = current.weather[0].main
            binding.textFeelsLike.text =
                "Feels Like : " + current.feels_like.minus(273.15F).roundToInt().toString()

            Glide.with(requireContext())
                .load("http://openweathermap.org/img/wn/" + current.weather[0].icon + ".png")
                .into(binding.imgTemperature)

            val pattern = "hh:mm a"

            val sunriseDate = getConvertedDate(current.sunrise.toLong())
            binding.sunrise.text =
                sunriseDate.toLocalTime().format(DateTimeFormatter.ofPattern(pattern))

            val sunsetDate = getConvertedDate(current.sunset.toLong())
            binding.sunset.text =
                sunsetDate.toLocalTime().format(DateTimeFormatter.ofPattern(pattern))

            binding.wind.text = current.wind_speed.toString() + " m/sec"
            binding.humidity.text = current.humidity.toString() + " %"
            binding.uv.text = current.uvi.toString()
            binding.rain.text = current.clouds.toString() + " %"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setViewModelObservers() {
        viewModel.getWeatherData(preferences.getString("latitude", "").toString(),
            preferences.getString("longitude", "").toString())
            .observe(viewLifecycleOwner, Observer { response ->
                when (response.status) {
                    Resource.Status.SUCCESS -> {
                        Log.wtf("response", response.data.toString())
                        data = response.data!!
                        setData()
                    }
                    Resource.Status.ERROR -> {
                    }

                    Resource.Status.LOADING -> {
                    }
                }
            })
    }
}