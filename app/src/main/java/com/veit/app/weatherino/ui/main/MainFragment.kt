package com.veit.app.weatherino.ui.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.veit.app.weatherino.R
import com.veit.app.weatherino.adapter.SwipeAction
import com.veit.app.weatherino.data.BookmarkedWeatherInfo
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
        requireActivity().removeMenuProvider(this)
        requireActivity().addMenuProvider(this)

        viewModel.currentWeather.observe(viewLifecycleOwner) {
            binding.currentWeatherResource = it
        }
        viewModel.bookmarkedWeathers.observe(viewLifecycleOwner) {
            binding.bookmarksResource = it
        }

        binding.bookmarksSwipeAction = object : SwipeAction<BookmarkedWeatherInfo> {
            override fun onItemSwiped(item: BookmarkedWeatherInfo) {
                viewModel.deleteBookmark(item)
            }
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
            R.id.menu_settings -> {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
                true
            }
            else -> false
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val time = LocalDate.of(year, month + 1, dayOfMonth)
            .atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toEpochSecond()
        val editText = EditText(requireContext()).apply {
            hint = getString(R.string.name)
            inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            imeOptions = EditorInfo.IME_ACTION_DONE
        }
        AlertDialog.Builder(requireContext())
            .setView(editText)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.addBookmark(editText.text.toString(), time)
            }
            .setMessage(R.string.bookmark_name_title)
            .create().show()
    }
}