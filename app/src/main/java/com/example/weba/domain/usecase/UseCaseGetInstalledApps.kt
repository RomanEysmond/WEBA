package com.example.weba.domain.usecase

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.weba.domain.models.AppInfo
import java.io.File
import java.security.MessageDigest

class UseCaseGetInstalledApps {

    fun getInstalledApps(packageManager: PackageManager, appsInfo: MutableLiveData<List<AppInfo>>) {
        val appsInfoData = mutableListOf<AppInfo>()

        // Получаем список приложений (совместимый способ)
        val apps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        }

        val userPackages = apps.filter { !isSystemPackage(it) }

        for (app in userPackages) {
            try {
                // Получаем информацию о пакете (совместимый способ)
                val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager.getPackageInfo(
                        app.packageName,
                        PackageManager.PackageInfoFlags.of(0)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    packageManager.getPackageInfo(app.packageName, 0)
                }

                val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode.toString()
                } else {
                    @Suppress("DEPRECATION")
                    packageInfo.versionCode.toString()
                }

                appsInfoData.add(
                    AppInfo(
                        name = app.loadLabel(packageManager).toString(),
                        version = packageInfo.versionName ?: "N/A",
                        packageName = packageInfo.packageName,
                        sha256 = calculateChecksum(app.sourceDir)
                    )
                )

                // Обновляем LiveData после обработки каждого приложения
                appsInfo.postValue(appsInfoData.toList())

                Log.d("AppInfo", "Processed: ${app.packageName}")

            } catch (e: Exception) {
                Log.e("AppInfoError", "Error processing ${app.packageName}: ${e.message}")
            }
        }
    }

    private fun isSystemPackage(appInfo: ApplicationInfo): Boolean {
        return (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 ||
                (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
    }

    private fun calculateChecksum(filePath: String): String {
        return try {
            val file = File(filePath)
            if (!file.exists()) return "File not found"

            val digest = MessageDigest.getInstance("SHA-256")
            file.inputStream().use { input ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    digest.update(buffer, 0, bytesRead)
                }
            }
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}