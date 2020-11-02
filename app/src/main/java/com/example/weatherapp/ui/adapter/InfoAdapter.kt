package com.example.weatherapp.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.utils.Utils
import com.example.weatherapp.databinding.ItemDaysBinding
import com.example.weatherapp.model.WeatherResponse

class InfoAdapter : RecyclerView.Adapter<InfoAdapter.MyViewHolder>() {

    private var itemList = ArrayList<WeatherResponse.Daily>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemDaysBinding = ItemDaysBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
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

    class MyViewHolder(private val itemDaysBinding: ItemDaysBinding) : RecyclerView.ViewHolder(
        itemDaysBinding.root
    ) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(weatherItem: WeatherResponse.Daily) {
            itemDaysBinding.day.text =
                Utils.getConvertedDate(weatherItem.dt.toLong()).dayOfWeek.toString()
            itemDaysBinding.textTemperature.text = weatherItem.weather[0].main
            Glide.with(itemDaysBinding.root)
                .load("http://openweathermap.org/img/wn/" + weatherItem.weather[0].icon + ".png")
                .into(itemDaysBinding.imgTemperature)
        }
    }
}