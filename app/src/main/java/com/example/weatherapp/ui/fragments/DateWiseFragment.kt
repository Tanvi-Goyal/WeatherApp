package com.example.weatherapp.ui.fragments

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.weatherapp.utils.Utils
import com.example.weatherapp.databinding.FragmentDateWiseBinding
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.remote.Resource
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class DateWiseFragment : Fragment() {

    private lateinit var binding: FragmentDateWiseBinding
    private val cities = arrayOf<String>("Delhi", "Mumbai", "Noida")
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var data: WeatherResponse
    private var selectedCity = ""
    private var timeStamp = ""

    @Inject
    lateinit var preferences: SharedPreferences
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDateWiseBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCityDialog()
        setDatePicker()
        setGetDataListener()
    }

    private fun setCityDialog() {
        binding.selectCity.setOnClickListener { view ->
            AlertDialog.Builder(requireContext())
                .setSingleChoiceItems(cities, 0, null)
                .setPositiveButton("Okay") { dialog, whichButton ->
                    dialog.dismiss()
                    val selectedPosition =
                        (dialog as AlertDialog).listView
                            .checkedItemPosition
                    selectedCity = cities[selectedPosition]
                }
                .show()
        }
    }

    private fun setDatePicker() {

        binding.selectDate.setOnClickListener {
            val c = Calendar.getInstance()

            c.add(Calendar.DAY_OF_MONTH, -5)
            val result: Date = c.time

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    timeStamp = (Date(year, monthOfYear, dayOfMonth).time / 1000L).toString()
                },
                year,
                month,
                day
            )

            dpd.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setGetDataListener() {
        binding.btnGetData.setOnClickListener {
            if (selectedCity != "" && timeStamp != "") {
                val latLon = Utils.getLatLongByCityName(selectedCity)

                viewModel.getWeatherByDate(latLon.lat, latLon.lon, timeStamp)
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
            } else {
                Toast.makeText(requireContext(), "Please Select City and Date", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {

        if (::data.isInitialized) {
            binding.layout.visibility = View.VISIBLE
            val current = data.current

            binding.locationName.text = selectedCity

            val date = Utils.getConvertedDate(current.dt.toLong())
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

            val sunriseDate = Utils.getConvertedDate(current.sunrise.toLong())
            binding.sunrise.text =
                sunriseDate.toLocalTime().format(DateTimeFormatter.ofPattern(pattern))

            val sunsetDate = Utils.getConvertedDate(current.sunset.toLong())
            binding.sunset.text =
                sunsetDate.toLocalTime().format(DateTimeFormatter.ofPattern(pattern))

            binding.wind.text = current.wind_speed.toString() + " m/sec"
            binding.humidity.text = current.humidity.toString() + " %"
            binding.uv.text = current.uvi.toString()
            binding.rain.text = current.clouds.toString() + " %"
        }
    }
}