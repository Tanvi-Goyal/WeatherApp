package com.example.weatherapp.ui.adapter

import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ItemDaysBinding
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.utils.Utils
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class InfoAdapter(private var preferences: SharedPreferences) : RecyclerView.Adapter<InfoAdapter.MyViewHolder>() {

    private var itemList = ArrayList<WeatherResponse.Daily>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemDaysBinding = ItemDaysBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding, preferences)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = itemList[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int = itemList.size

    fun addWeatherInfo(weatherData: List<WeatherResponse.Daily>) {
        itemList.clear()
        itemList.addAll(weatherData)
        notifyDataSetChanged()
    }

    class MyViewHolder(
        private val itemDaysBinding: ItemDaysBinding,
        private val preferences: SharedPreferences
    ) : RecyclerView.ViewHolder(
        itemDaysBinding.root
    ) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(weatherItem: WeatherResponse.Daily) {
            itemDaysBinding.day.text =
                Utils.getConvertedDate(weatherItem.dt.toLong()).dayOfWeek.toString()

            itemDaysBinding.textTemperature.text =
                if (preferences.getString("temperature_type", "").equals("fahrenheit")) {
                    ((9 / 5) * ((weatherItem.temp.day.roundToInt().minus(273.15F)
                        .roundToInt()).plus(32))).toString() + " \u2109" + " " + weatherItem.weather[0].main
                } else {
                    weatherItem.temp.day.roundToInt().minus(273.15F).roundToInt().toString() + " \u2103" + " " + weatherItem.weather[0].main
                }

//            itemDaysBinding.textTemperature.text = weatherItem.weather[0].main

//            val iconUrl =
//                "http://openweathermap.org/img/w/" + weatherItem.weather[0].icon + ".png"
//
//            Glide.with(itemDaysBinding.root).load(iconUrl)
//                .into(itemDaysBinding.imgTemperature)

            val icon: String = weatherItem.weather[0].icon
            val iconUrl = "http://openweathermap.org/img/w/$icon.png"

            Picasso.get().load(iconUrl).into(itemDaysBinding.imgTemperature)
        }
    }
}