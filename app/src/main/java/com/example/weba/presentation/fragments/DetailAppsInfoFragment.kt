package com.example.weba.presentation.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weba.R
import com.example.weba.databinding.DetailAppsInfoFragmentBinding
import com.example.weba.domain.models.AppInfo
import com.example.weba.presentation.recyclerview.MyAdapter

@SuppressLint("StaticFieldLeak")
private var _binding: DetailAppsInfoFragmentBinding? = null
private const val ARG_PARAM1 = "index"
private val REQUEST_QUERY_ALL_PACKAGES = 1001

class DetailAppsInfoFragment : Fragment() {
    private val binding get() = _binding!!
    private val viewModel: AppsInfoViewModel by activityViewModels()
    private var param1: Int? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailAppsInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        requestQueryAllPackagesPermission()

    }


    private fun loadAppsInfo() {
        viewModel.loadApps(requireContext().packageManager)
    }

    private fun updateUI(apps: List<AppInfo>) {
        binding.recyclerView.adapter = MyAdapter(apps) { index ->
            param1 = index
            val bundle = Bundle().apply {
                putInt(ARG_PARAM1, param1!!)}
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment,bundle)
        }

    }

    private fun observeViewModel() {
        binding.swipeRefresh.isEnabled = false
        viewModel.loadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AppsInfoViewModel.LoadState.Loading -> {
                    binding.circularProgress.visibility = View.VISIBLE
                    binding.progressText.text = getString(R.string.downloading)
                    binding.recyclerView.visibility = View.GONE
                }

                is AppsInfoViewModel.LoadState.Success -> {
                    binding.circularProgress.visibility = View.GONE
                    binding.progressText.text = getString(R.string.success)
                    binding.recyclerView.visibility = View.VISIBLE

                    // Устанавливаем данные в RecyclerView
                    binding.progressText.visibility = View.GONE
                }

                is AppsInfoViewModel.LoadState.Error -> {
                    binding.circularProgress.visibility = View.GONE
                    binding.progressText.text = getString(R.string.error_loading, state.message)
                    Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun requestQueryAllPackagesPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val shouldShowRationale = shouldShowRequestPermissionRationale(
                android.Manifest.permission.QUERY_ALL_PACKAGES
            )

            if (shouldShowRationale) {
                // Показать объяснение перед запросом
                showPermissionRationaleDialog()
            } else {
                // Запросить напрямую
                requestPermissions(
                    arrayOf(android.Manifest.permission.QUERY_ALL_PACKAGES),
                    REQUEST_QUERY_ALL_PACKAGES
                )
            }
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Требуется разрешение")
            .setMessage("Для отображения списка всех приложений необходимо разрешение")
            .setPositiveButton("OK") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.QUERY_ALL_PACKAGES),
                    REQUEST_QUERY_ALL_PACKAGES
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_QUERY_ALL_PACKAGES -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    viewModel.appsList.observe(viewLifecycleOwner) { apps ->
                        updateUI(apps)
                    }

                    observeViewModel()

                    if (viewModel.appsList.value == null) {
                        loadAppsInfo()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Функциональность ограничена без разрешения",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}