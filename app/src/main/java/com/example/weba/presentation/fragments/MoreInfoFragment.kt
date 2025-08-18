package com.example.weba.presentation.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.weba.R
import com.example.weba.databinding.MoreInfoFragmentBinding
import com.example.weba.domain.models.AppInfo

@SuppressLint("StaticFieldLeak")
private var _binding: MoreInfoFragmentBinding? = null
private const val ARG_PARAM1 = "index"

class MoreInfoFragment : Fragment() {

    private val binding get() = _binding!!
    private val viewModel: DetailAppsInfoViewModel by activityViewModels()
    private var index: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            index = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoreInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.appsList.value?.let { updateUI(it.get(this.index!!)) }
    }

    private fun updateUI(app: AppInfo) {

        binding.appName.text = getString(R.string.app_name_is_the, app.name)
        binding.packageName.text = getString(R.string.package_name_is, app.packageName)
        binding.appVersion.text = getString(R.string.app_version_is, app.version)
        binding.sha256.text =
            getString(R.string.the_checksum_of_the_apk_file_is, app.sha256)

        binding.buttonOpenApp.setOnClickListener {
            openApp(packageName = app.packageName)
        }
    }

    private fun openApp(packageName: String) {
        val launchIntent = requireContext().packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launchIntent)
        }
    }

}