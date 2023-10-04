package com.vr.superexambro.lockutils

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.os.UserHandle
import android.widget.Toast
import com.vr.superexambro.R

class LockDeviceAdminReceiver : DeviceAdminReceiver() {
    fun showToast(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onEnabled(context: Context, intent: Intent) {
        //
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence? {
        return context.getString(R.string.admin_receiver_status_disable_warning)
    }

    override fun onDisabled(context: Context, intent: Intent) {
        showToast(context, context.getString(R.string.admin_receiver_status_disable_warning))
    }

    override fun onPasswordChanged(context: Context, intent: Intent, userHandle: UserHandle) {
        //
    }
}