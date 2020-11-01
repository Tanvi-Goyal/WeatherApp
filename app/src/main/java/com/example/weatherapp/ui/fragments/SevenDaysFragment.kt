package com.example.weatherapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.FragmentSevenDaysBinding
import com.example.weatherapp.remote.Resource
import com.example.weatherapp.ui.adapter.InfoAdapter
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SevenDaysFragment : Fragment() {

    private lateinit var binding: FragmentSevenDaysBinding
    private lateinit var infoAdapter: InfoAdapter
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSevenDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()
        setViewModelObservers()
    }

    private fun setUpRecycler() {
        infoAdapter = InfoAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = infoAdapter
    }

    private fun setViewModelObservers() {
        viewModel.getWeatherData("28.73", "77.20")
            .observe(viewLifecycleOwner, Observer { response ->
                when (response.status) {
                    Resource.Status.SUCCESS -> {
                        response.data?.daily?.let { infoAdapter.addWeatherInfo(it) }
                    }
                    Resource.Status.ERROR -> {
                    }

                    Resource.Status.LOADING -> {
                    }
                }
            })
    }
}