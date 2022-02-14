package com.example.package_application_task1

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akshay_taru.package_library.MyPackageInfo
import com.akshay_taru.package_library.entites.AppInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MainViewModel"
    private val _appList: MutableLiveData<List<AppInfo>> = MutableLiveData(mutableListOf())
    val appList: LiveData<List<AppInfo>> = _appList
    private val installAppsList: MutableList<AppInfo> = mutableListOf()
    var job: Job = viewModelScope.launch {

        MyPackageInfo.listenAppChanges(context = application.applicationContext).collectLatest {
            Log.d(TAG, "Received event: ")
            addData(application.applicationContext)
        }
    }

    init {
        addData(application.applicationContext)
    }

    private fun addData(context: Context) {
        installAppsList.clear()
        installAppsList.addAll(MyPackageInfo.getPackages(context))
        _appList.postValue(installAppsList)
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}