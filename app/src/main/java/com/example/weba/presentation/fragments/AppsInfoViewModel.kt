package com.example.weba.presentation.fragments

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weba.domain.usecase.UseCaseGetInstalledApps
import com.example.weba.domain.models.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AppsInfoViewModel(application: Application) : AndroidViewModel(application) {

    sealed class LoadState {
        object Loading : LoadState()
        object Success : LoadState()
        data class Error(val message: String) : LoadState()
    }

    private val _loadState = MutableLiveData<LoadState>()
    val loadState: LiveData<LoadState> = _loadState

    private val _appsList = MutableLiveData<List<AppInfo>>()
    val appsList: LiveData<List<AppInfo>> = _appsList

   /* private val prefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val _data = MutableLiveData<List<AppInfo>>()
    val data: LiveData<List<AppInfo>> = _data

    init {
        // Загрузка при инициализации
        _data.value = prefs.getString("key_data", null)
    }*/

    fun loadApps(packageManager: PackageManager) {
        viewModelScope.launch(Dispatchers.Default) {
            _loadState.postValue(LoadState.Loading)
            UseCaseGetInstalledApps().getInstalledApps(
                packageManager = packageManager,
                _appsList
            )
            _loadState.postValue(LoadState.Success)

        }

    }
}