package com.mobatia.bisad.constants

import android.content.Context
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException

class DebuggingChecker {
    fun isUsbDebuggingEnabled(context: Context): Boolean {
        try {
            val adbEnabled = Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.ADB_ENABLED
            )
            return adbEnabled == 1
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
            return false
        }
    }
}