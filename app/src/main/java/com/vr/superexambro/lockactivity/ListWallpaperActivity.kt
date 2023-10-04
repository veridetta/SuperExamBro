package com.vr.superexambro.lockactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdListener
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.facebook.ads.AudienceNetworkAds
import com.vr.superexambro.R
import com.vr.superexambro.lockutils.RecyclerItemClickListener
import com.vr.superexambro.lockutils.Wallpaper
import com.vr.superexambro.lockutils.WallpaperAdapter
import java.util.Random

class ListWallpaperActivity : AppCompatActivity() {
    private val wallpapersList: MutableList<Wallpaper> = ArrayList<Wallpaper>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: WallpaperAdapter
    private lateinit var back: ImageView
    private lateinit var adView: AdView

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_wallpaper)
        recyclerView = findViewById(R.id.rcw_icream1)
        back = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener { finish() })
        mAdapter = WallpaperAdapter(wallpapersList)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@ListWallpaperActivity)
        recyclerView.setLayoutManager(mLayoutManager)
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.setLayoutManager(
            GridLayoutManager(
                this@ListWallpaperActivity,
                2,
                GridLayout.VERTICAL,
                false
            )
        )
        recyclerView.setAdapter(mAdapter)
        prepareMovieData()
        loadBannerFB()
        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this@ListWallpaperActivity,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val intent = Intent(this@ListWallpaperActivity, SetWallpaperActivity::class.java)
                        intent.putExtra("url", wallpapersList[position].url)
                        startActivity(intent)

                    }

                    override fun onLongItemClick(view: View?, position: Int) {}
                })
        )
    }
    private fun prepareMovieData() {
        var wallpaper = Wallpaper(R.mipmap.a1s, R.mipmap.a1)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a2s, R.mipmap.a2)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a3s, R.mipmap.a3)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a4s, R.mipmap.a4)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a5s, R.mipmap.a5)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a6s, R.mipmap.a6)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a7s, R.mipmap.a7)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a8s, R.mipmap.a8)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a9s, R.mipmap.a9)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a10s, R.mipmap.a10)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a11s, R.mipmap.a11)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a12s, R.mipmap.a12)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a13s, R.mipmap.a13)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a14s, R.mipmap.a14)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a15s, R.mipmap.a15)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a16s, R.mipmap.a16)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a17s, R.mipmap.a17)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a18s, R.mipmap.a18)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a19s, R.mipmap.a19)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a20s, R.mipmap.a20)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a21s, R.mipmap.a21)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a22s, R.mipmap.a22)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a23s, R.mipmap.a23)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a24s, R.mipmap.a24)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a25s, R.mipmap.a25)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a26s, R.mipmap.a26)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a27s, R.mipmap.a27)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a28s, R.mipmap.a28)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a29s, R.mipmap.a29)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a30s, R.mipmap.a30)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a31s, R.mipmap.a31)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a32s, R.mipmap.a32)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a33s, R.mipmap.a33)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a34s, R.mipmap.a34)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a35s, R.mipmap.a35)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a36s, R.mipmap.a36)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a37s, R.mipmap.a37)
        wallpapersList.add(wallpaper)
        wallpaper = Wallpaper(R.mipmap.a38s, R.mipmap.a38)
        wallpapersList.add(wallpaper)
        mAdapter.notifyDataSetChanged()
    }

    fun loadBannerGG() {
        val adContainer = findViewById<View>(R.id.adMobView)
    }

    fun loadBannerFB() {
        val adContainer = findViewById<View>(R.id.adMobView)
        AudienceNetworkAds.initialize(this)
        adView = AdView(this, MainActivity.BANNER_ID_FB, AdSize.BANNER_HEIGHT_50)
        (adContainer as LinearLayout).addView(adView)

// Request an ad
        val adListener: AdListener = object : AdListener {
            override fun onError(ad: Ad, adError: AdError) {
                loadBannerGG()
            }

            override fun onAdLoaded(ad: Ad) {
                // Ad loaded callback
                adContainer.setBackgroundColor(Color.GRAY)
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
            }
        }

        // Request an ad
        adView!!.loadAd(adView!!.buildLoadAdConfig().withAdListener(adListener).build())
        val r = Random()
        val ads = r.nextInt(100)
        if (ads >= MainActivity.PERCENT_SHOW_BANNER_AD) {
            adView!!.destroy()
            adView!!.visibility = View.GONE
        }
    }
}