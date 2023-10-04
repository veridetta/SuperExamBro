package com.vr.superexambro.activity

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar
import com.vr.superexambro.R
import com.vr.superexambro.activity.guru.LoginActivity
import com.vr.superexambro.activity.guru.auth.GuruActivity
import com.vr.superexambro.activity.siswa.SiswaActivity
import com.vr.superexambro.helper.initNoInternetLayout
import com.vr.superexambro.helper.isInternetAvailable

class RoleActivity : AppCompatActivity() {
    //init elemen
    lateinit var cardSiswa : CardView
    lateinit var cardGuru : CardView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var isLogin: Boolean = false
    var isKoneksi: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role)
        initView()
        elementClick()
        checkLogin()
    }

    fun initView(){
        cardSiswa = findViewById(R.id.card_siswa)
        cardGuru = findViewById(R.id.card_guru)
        initNoInternetLayout(this, R.id.noInternet)
        isKoneksi = isInternetAvailable(this, R.id.noInternet)

    }
    fun elementClick(){
        cardSiswa.setOnClickListener {
            //pindah ke activity siswa
            intent = intent.setClass(this, SiswaActivity::class.java)
            startActivity(intent)
            finish()
        }
        cardGuru.setOnClickListener {
            //pindah ke activity guru
            intent = intent.setClass(this, GuruActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun checkLogin(){
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        isLogin = sharedPreferences.getBoolean("isLogin", false)
        if (isLogin){
            //jika sudah login
            //pindah ke main activity
            intent = intent.setClass(this, GuruActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            dialogSupport()
        }
    }
    private fun dialogSupport() {
        val dialog2 = Dialog(this)
        dialog2.setContentView(R.layout.dialog_question)
        dialog2.show()
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog2.window!!.attributes)
        layoutParams.width = displayWidth
        layoutParams.height = displayHeight
        dialog2.window!!.attributes = layoutParams
        val setPer1 = dialog2.findViewById<ImageView>(R.id.per1)
        setPer1.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }
        val packageName = this.packageName
        val setPer2 = dialog2.findViewById<ImageView>(R.id.per2)
        setPer2.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        val back = dialog2.findViewById<ImageView>(R.id.back)
        back.setOnClickListener { dialog2.cancel() }
    }
}