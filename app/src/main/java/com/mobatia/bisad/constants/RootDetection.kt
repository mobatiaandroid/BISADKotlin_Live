package com.mobatia.bisad.constants

import java.io.File

public  class RootDetection {
 public   fun isDeviceRooted(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )

        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }

        // Additional check to see if the `su` command can be executed
        return canExecuteSu()
    }

    // Method to check if the `su` command can be executed
    fun canExecuteSu(): Boolean {
        try {
            val process = Runtime.getRuntime().exec("su")
            process.destroy()
            return true
        } catch (e: Exception) {
            return false
        }
    }
}