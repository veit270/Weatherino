package com.veit.app.weatherino.ui.day_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.veit.app.weatherino.databinding.FragmentDayDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayDetailsFragment : Fragment() {

    private val viewModel: DayDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDayDetailsBinding.inflate(inflater, container, false).root
    }
}