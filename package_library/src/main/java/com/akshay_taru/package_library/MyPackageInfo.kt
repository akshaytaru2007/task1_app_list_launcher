package com.akshay_taru.package_library

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.ChangedPackages
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.akshay_taru.package_library.entites.AppInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object MyPackageInfo {
    private var accessCount: Int = 0

    private const val TAG = "MyPackageInfo"

    fun getPackages(context: Context): List<AppInfo> {
        val installedPackages: MutableList<AppInfo> = ArrayList()
        val packageManager: PackageManager =  context.packageManager
        val packages: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        for (packageInfo in packages) {
            if (packageInfo.applicationInfo != null && packageInfo.applicationInfo.flags != ApplicationInfo.FLAG_SYSTEM) {
                packageInfo.versionName?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        installedPackages.add(
                            AppInfo(
                                icon = packageInfo.applicationInfo.loadIcon(packageManager),
                                packageName = packageInfo.packageName,
                                versionCode = packageInfo.longVersionCode,
                                versionName = packageInfo.packageName,
                                name = packageInfo.applicationInfo.loadLabel(packageManager)
                                    .toString()
                            )
                        )
                    } else {
                        installedPackages.add(
                            AppInfo(
                                icon = packageInfo.applicationInfo.loadIcon(packageManager),
                                packageName = packageInfo.packageName,
                                versionCode = packageInfo.versionCode.toLong(),
                                versionName = packageInfo.packageName,
                                name = packageInfo.applicationInfo.loadLabel(packageManager)
                                    .toString()
                            )
                        )
                    }
                }
            }
        }
        if (installedPackages.isNotEmpty())
            installedPackages.sortBy { it.name }

        for (packageInfo in installedPackages) {
            Log.d(TAG, "App name :" + packageInfo.name)
        }

        return installedPackages
    }

    /**
     * Emit event when new packages are added or removed
     */
    fun listenAppChanges(intervalMilliSec: Long = 10000, context: Context): Flow<List<String>> = flow {
        val packageManager: PackageManager =  context.packageManager

        while (true) {
            val changedPackages: ChangedPackages? = packageManager.getChangedPackages(accessCount)
            changedPackages?.let {
               emit(it.packageNames)
            }
            accessCount ++
            delay(intervalMilliSec)
        }
    }
}