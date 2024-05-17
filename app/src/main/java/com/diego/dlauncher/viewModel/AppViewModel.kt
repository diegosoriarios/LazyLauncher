package com.diego.dlauncher.viewModel;

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.diego.dlauncher.model.AppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class AppUIState(
    val apps: ArrayList<AppInfo> = arrayListOf(),
    val favorites: ArrayList<AppInfo> = arrayListOf()
)


class AppViewModel(context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUIState())
    val uiState: StateFlow<AppUIState> = _uiState.asStateFlow()
    lateinit var context: Context

    init {
        this.context = context
    }

    fun loadApps(packageManager: PackageManager) {
        try {
            if (uiState.value.apps.size == 0) {
                val i = Intent(Intent.ACTION_MAIN, null)
                i.addCategory(Intent.CATEGORY_LAUNCHER)
                if (packageManager != null) {
                    val availableApps: List<ApplicationInfo> =
                        packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

                    var newApps = arrayListOf<AppInfo>()
                    for (packageInfo in availableApps) {
                        if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) == null) {
                            continue
                        }
                            val appInfo = AppInfo()
                            appInfo.name = packageInfo.packageName
                            appInfo.icon = packageManager.getApplicationIcon(packageInfo).toBitmap().asImageBitmap()
                            appInfo.label = packageManager.getApplicationLabel(packageInfo)
                            newApps.add(appInfo)

                    }
                    val sortedApps = newApps.sortedBy { it.label.toString() }
                    _uiState.update {
                        it.copy(
                            apps = ArrayList(sortedApps),
                            favorites = arrayListOf(sortedApps[0], sortedApps[1])
                        )
                    }
//
//                    for (ri in availableApps) {
//                        val appInfo = AppInfo()
//                        appInfo.label = ri.loadLabel(packageManager)
//                        appInfo.name = ri.activityInfo.packageName
//                        appInfo.icon = ri.activityInfo.loadIcon(packageManager)
//                        (apps as ArrayList<AppInfo>).add(appInfo)
//                    }
//                    if (apps != null) {
//                        for (i : AppInfo in apps!!) {
//                            Log.d("AQUI", i.name.toString())
//                        }
//                    }
                }
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Error loadApps", ex.message.toString() + " loadApps")
        }
    }

    fun uninstallApp(appInfo: AppInfo) {
        val packageName = appInfo.name // Replace with the package name of the app you want to uninstall

        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        val options = Bundle()
        startActivity(this.context, intent, options)
    }

    fun openConfiguration(appInfo: AppInfo) {
        val packageName = appInfo.name // Replace with the package name of the app you want to uninstall

        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        val options = Bundle()
        startActivity(this.context, intent, options)
    }
}
