package com.vr.superexambro.lock

import android.app.Application
import com.flurry.android.FlurryAgent

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FlurryAgent.Builder()
            .withLogEnabled(true)
            .build(this, "BT5S7RM2Y5HNTCG5XNM7")
    }
}
