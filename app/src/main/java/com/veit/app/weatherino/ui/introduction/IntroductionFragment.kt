package com.veit.app.weatherino.ui.introduction

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.veit.app.weatherino.R
import com.veit.app.weatherino.databinding.FragmentIntroductionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionFragment : Fragment() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                navigateToMain()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.permission_not_granted_title)
                    .setMessage(R.string.permission_not_granted_message)
                    .setPositiveButton(R.string.ok, null)
                    .create().show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if(ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            navigateToMain()
            null
        } else {
            FragmentIntroductionBinding.inflate(inflater, container, false).apply {
                prepareUi(this)
            }.root
        }
    }

    private fun prepareUi(binding: FragmentIntroductionBinding) {
        binding.requestPermissionButton.setOnClickListener { requestPermission() }
    }

    private fun navigateToMain() {
        findNavController().navigate(IntroductionFragmentDirections.actionIntroductionFragmentToMainFragment())
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}