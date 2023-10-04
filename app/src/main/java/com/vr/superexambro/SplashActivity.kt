package com.vr.superexambro

import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.vr.superexambro.activity.RoleActivity
import com.vr.superexambro.helper.initNoInternetLayout
import com.vr.superexambro.helper.isInternetAvailable
import com.vr.superexambro.lockactivity.MainActivity

class SplashActivity : AppCompatActivity() {
    // This is the splash screen activity
    //lateinit
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var isLogin: Boolean = false
    lateinit var progressBar : TextRoundCornerProgressBar
    var isKoneksi: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
        initView()

        if (isKoneksi){
            //jika ada koneksi
            checkLogin()
            progressBar.setProgress(100)
        }else{
            //jika tidak ada koneksi
            isInternetAvailable(this, R.id.noInternet)
        }
    }

    fun initView(){
        progressBar = findViewById(R.id.progressBar)
        initNoInternetLayout(this, R.id.noInternet)
        isKoneksi = isInternetAvailable(this, R.id.noInternet)
    }

    fun checkLogin(){
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        isLogin = sharedPreferences.getBoolean("isLogin", false)
        if (isLogin){
            //jika sudah login
            //pindah ke main activity
            intent = intent.setClass(this, RoleActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            //jika belum login
            intent = intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun progressBar(){
        //progress bar
        progressBar.setMax(100)
        progressBar.setProgress(0)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            var n = 0
            Thread(Runnable {
                while (progressBar.getProgress() < 98){
                    try {
                        Thread.sleep(98)
                    }catch (e: InterruptedException){
                        e.printStackTrace()
                    }
                    n +=10
                    progressBar.setProgress(n)
                }
            }).start()
        },100)
    }
}