package com.example.weba.presentation.fragments

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weba.domain.usecase.UseCaseGetInstalledApps
import com.example.weba.domain.models.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AppsInfoViewModel : ViewModel() {

    sealed class LoadState {
        object Loading : LoadState()
        object Success : LoadState()
        data class Error(val message: String) : LoadState()
    }

    private val _loadState = MutableLiveData<LoadState>()
    val loadState: LiveData<LoadState> = _loadState

    private val _appsList = MutableLiveData<List<AppInfo>>()
    val appsList: LiveData<List<AppInfo>> = _appsList


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