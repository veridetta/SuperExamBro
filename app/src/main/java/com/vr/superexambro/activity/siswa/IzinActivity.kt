package com.vr.superexambro.activity.siswa

import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.LinearLayout
import com.vr.superexambro.R
import com.vr.superexambro.lockactivity.ChangPassWordActivity
import com.vr.superexambro.lockactivity.MainActivity
import com.vr.superexambro.lockutils.LockScreen

class IzinActivity : AppCompatActivity() {
    lateinit var btnDisable : LinearLayout
    lateinit var btnShow : LinearLayout
    //lateinit var btnLock : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_izin)
        initView()
        initClick()
    }

    private fun initView(){
        btnDisable = findViewById(R.id.btnDisable)
        btnShow = findViewById(R.id.btnShow)
        //btnLock = findViewById(R.id.btnLock)
    }
    private fun initClick(){
        btnDisable.setOnClickListener {
            val intent = Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD)
            startActivity(intent)
        }
        btnShow.setOnClickListener{
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        /*btnLock.setOnClickListener{ view ->

        }*/

    }
}