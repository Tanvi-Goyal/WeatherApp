package com.example.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.Converters
import com.example.weatherapp.databinding.ItemDaysBinding
import com.example.weatherapp.model.WeatherResponse

class InfoAdapter() : RecyclerView.Adapter<InfoAdapter.MyViewHolder>() {

    private var itemList = ArrayList<WeatherResponse.Daily>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemDaysBinding = ItemDaysBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

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

    class MyViewHolder(private val itemHeadlineBinding: ItemDaysBinding) : RecyclerView.ViewHolder(
        itemHeadlineBinding.root
    ) {
        fun bind(weatherItem: WeatherResponse.Daily) {
            itemHeadlineBinding.day.text = weatherItem.dt.toString()
            itemHeadlineBinding.textTemperature.text = weatherItem.weather[0].main
            Glide.with(itemHeadlineBinding.root)
                .load(weatherItem.weather[0].icon)
                .into(itemHeadlineBinding.imgTemperature)
        }
    }
}