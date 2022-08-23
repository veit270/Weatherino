package com.veit.app.weatherino.ui.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.veit.app.weatherino.R
import com.veit.app.weatherino.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment(), MenuProvider, DatePickerDialog.OnDateSetListener {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMainBinding.inflate(inflater, container, false).apply {
            prepareUi(this)
        }.root
    }

    private fun prepareUi(binding: FragmentMainBinding) {
        requireActivity().addMenuProvider(this)
        viewModel.currentWeather.observe(viewLifecycleOwner) {
            binding.currentWeatherResource = it
        }

        binding.addBookmarkDate.setOnClickListener { pickBookmarkDate() }
    }

    private fun pickBookmarkDate() {
        val today = LocalDate.now()
        DatePickerDialog(
            requireContext(),
            this,
            today.year,
            today.monthValue - 1,
            today.dayOfMonth
        ).apply {
            datePicker.minDate = Date().time
        }.show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_refresh -> {
                viewModel.refreshAll()
                true
            }
            else -> false
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        LocalDate.of(year, month, dayOfMonth).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}