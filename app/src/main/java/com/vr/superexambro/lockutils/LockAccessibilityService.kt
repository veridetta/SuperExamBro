package com.vr.superexambro.lockutils

import android.accessibilityservice.AccessibilityService
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.vr.superexambro.lockactivity.MainActivity

class LockAccessibilityService : AccessibilityService() {
    override fun onKeyEvent(event: KeyEvent): Boolean {
        //lockscreen instance
        LockScreen.getInstance(this)
        if (MainActivity.lockScreenShow== true) {
            val keyCode = event.keyCode
            if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_MENU) {
                return true
            }
        }
        return super.onKeyEvent(event)
    }

    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent) {
        //
    }

    override fun onInterrupt() {}
}
