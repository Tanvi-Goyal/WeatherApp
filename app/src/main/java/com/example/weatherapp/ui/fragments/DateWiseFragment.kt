package com.example.weatherapp.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.weatherapp.databinding.FragmentDateWiseBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DateWiseFragment : Fragment() {

    private lateinit var binding: FragmentDateWiseBinding
    private val cities = arrayOf<String>("Delhi", "Mumbai", "Noida")
    private var selectedCity = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDateWiseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCityDialog()
        setDatePicker()
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

            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
//                lblDate.setText("" + dayOfMonth + " " + MONTHS[monthOfYear] + ", " + year)

            }, year, month, day)

//        dpd.setMaxDate(System.currentTimeMillis())
//        dpd.setMinDate(result.time)
            dpd.show()
        }
    }
}