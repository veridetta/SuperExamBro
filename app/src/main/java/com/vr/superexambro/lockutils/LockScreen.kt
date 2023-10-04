package com.vr.superexambro.lockutils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.provider.Settings

class LockScreen {
    private var context: Context? = null
    private var disableHomeButton = false
    fun init(context: Context?) {
        this.context = context
    }

    fun init(context: Context?, disableHomeButton: Boolean) {
        this.context = context
        this.disableHomeButton = disableHomeButton
    }

    private fun showSettingAccessibility() {
        if (!isMyServiceRunning(LockAccessibilityService::class.java)) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            context!!.startActivity(intent)
        }
    }

    fun active() {
        if (disableHomeButton) {
            showSettingAccessibility()
        }
        if (context != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                ContextCompat.startForegroundService(context, new Intent(context, LockScreenService.class));
//            } else {
//                context.startService(new Intent(context, LockScreenService.class));
//            }
//            context.startService(new Intent(context, LockScreenService.class));
//            ContextCompat.startForegroundService(context, new Intent(context, LockScreenService.class));
            AppUtils.startService(context!!, LockScreenService::class.java)
        }
    }

    fun deactivate() {
        if (context != null) {
            context!!.stopService(Intent(context, LockScreenService::class.java))
        }
    }

    val isActive: Boolean
        get() = if (context != null) {
            isMyServiceRunning(LockScreenService::class.java)
        } else {
            false
        }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    companion object {
        private var singleton: LockScreen? = null
        fun getInstance(context: Context?): LockScreen? {
            if (singleton == null) {
                singleton = LockScreen()
            }
            singleton!!.init(context)
            return singleton
        }
    }
}