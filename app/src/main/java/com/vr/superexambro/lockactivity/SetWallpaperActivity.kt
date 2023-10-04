package com.vr.superexambro.lockactivity

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vr.superexambro.R
import java.io.IOException

class SetWallpaperActivity : AppCompatActivity() {
    private lateinit var wallpaper: ImageView
    private  lateinit var setWallpaper:ImageView
    private  lateinit var back:ImageView
    private var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_wallpaper)
        val mIntent = intent
        index = mIntent.getIntExtra("index", R.mipmap.a1)
        wallpaper = findViewById<ImageView>(R.id.wallpaper)
        wallpaper.setImageResource(index)
        setWallpaper = findViewById<ImageView>(R.id.setWallpaper)
        setWallpaper.setOnClickListener(View.OnClickListener {
            MainActivity.showInterFb(this@SetWallpaperActivity)
            setWallpaper()
        })
        back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener(View.OnClickListener { finish() })
    }

    private fun setWallpaper() {
        val bitmap = BitmapFactory.decodeResource(resources, index)
        val manager = WallpaperManager.getInstance(applicationContext)
        try {
            manager.setBitmap(bitmap)
            Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }
    }
}