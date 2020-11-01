package com.example.weatherapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.weatherapp.databinding.FragmentCurrentBinding
import com.example.weatherapp.remote.Resource
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrentFragment : Fragment() {

    private lateinit var binding: FragmentCurrentBinding
    private val viewModel: WeatherViewModel by viewModels()

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModelObservers()
        binding.text.text = preferences.getString("username", "")
    }

    private fun setViewModelObservers() {
        viewModel.getWeatherData("28.73", "77.20")
            .observe(viewLifecycleOwner, Observer { response ->
                when (response.status) {
                    Resource.Status.SUCCESS -> {
                        Log.wtf("response", response.data.toString())
                    }
                    Resource.Status.ERROR -> {
                    }

                    Resource.Status.LOADING -> {
                    }
                }
            })
    }
}