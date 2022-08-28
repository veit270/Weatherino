package com.veit.app.weatherino.ui.settings

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.veit.app.weatherino.MainActivity
import com.veit.app.weatherino.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(), MenuProvider {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSettingsBinding.inflate(inflater, container, false).apply {
            prepareUi(this)
        }.root
    }

    private fun prepareUi(binding: FragmentSettingsBinding) {
        (requireActivity() as? MainActivity)?.run {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
        binding.viewModel = viewModel
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        //
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}