package com.example.weatherapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.weatherapp.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val temp = arrayOf<String>("Celsius", "Fahrenheit")

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTemperatureDialog()
    }

    private fun setTemperatureDialog() {
        binding.temperature.setOnClickListener { view ->
            AlertDialog.Builder(requireContext())
                .setSingleChoiceItems(temp, 0, null)
                .setPositiveButton("Okay") { dialog, whichButton ->
                    dialog.dismiss()
                    val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    preferences.edit()
                        .putString("temperature_type", temp[selectedPosition].toLowerCase()).apply()
                    binding.temperature.text = temp[selectedPosition]
                }
                .show()
        }
    }

}