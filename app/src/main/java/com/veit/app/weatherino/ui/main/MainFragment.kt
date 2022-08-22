package com.veit.app.weatherino.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.veit.app.weatherino.R
import com.veit.app.weatherino.databinding.FragmentMainBinding
import com.veit.app.weatherino.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), MenuProvider {

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
            binding.loading = it.status == Status.LOADING
            binding.currentWeather = it.data
        }
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
}