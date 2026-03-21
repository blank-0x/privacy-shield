package com.privacyshield

object RootFeatureGate {
    val enabled: Boolean get() = BuildConfig.ENABLE_ROOT_FEATURES

    fun isRooted(): Boolean {
        // Check su binary (real rooted devices with Magisk etc)
        try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
            process.waitFor(2, java.util.concurrent.TimeUnit.SECONDS)
            val result = process.inputStream.bufferedReader().readLine() ?: ""
            if (result.contains("uid=0")) return true
        } catch (e: Exception) {}

        // Check if current process is already running as root (ADB root / emulator)
        try {
            val process = Runtime.getRuntime().exec(arrayOf("id"))
            val result = process.inputStream.bufferedReader().readLine() ?: ""
            if (result.contains("uid=0")) return true
        } catch (e: Exception) {}

        return false
    }

    fun canUseRootFeatures(): Boolean {
        // If root features explicitly enabled via BuildConfig, trust it
        // (used for dev builds and emulator testing)
        if (BuildConfig.ENABLE_ROOT_FEATURES) return true
        return false
    }
}
