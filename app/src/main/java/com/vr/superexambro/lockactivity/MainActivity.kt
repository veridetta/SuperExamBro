package com.vr.superexambro.lockactivity

import android.app.AlertDialog
import android.app.Dialog
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdListener
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.facebook.ads.AudienceNetworkAds
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.vr.superexambro.R
import com.vr.superexambro.lockutils.BackUpModel
import com.vr.superexambro.lockutils.HttpHandler
import com.vr.superexambro.lockutils.LockScreen
import org.json.JSONException
import org.json.JSONObject
import java.util.Random
import java.util.concurrent.ExecutionException

class MainActivity : AppCompatActivity() {
    private lateinit var setLockScreen: LinearLayout
    private lateinit var setVideo: LinearLayout
    private lateinit var btn_lockScreen: ImageView
    private lateinit var btn_video: ImageView
    private lateinit var preview: ImageView
    private lateinit var changeQuestion: ImageView
    private lateinit var changePassWord: ImageView
    private lateinit var permission: ImageView
    private lateinit var disable_lock: ImageView
    private lateinit var support: ImageView
    private lateinit var backUpModel: BackUpModel
    private lateinit var adView: AdView
    private lateinit var wallpaper: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
        /*
        try {
            val aVoid: Void = GetBackUp().execute().get()!!
            if (backUpModel != null) {
                if (!backUpModel.isLive!!) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Notice from developer")
                        .setMessage("Please update the new application to continue using it. We are really sorry for this issue.")
                        .setPositiveButton(
                            android.R.string.yes
                        ) { dialog, which ->
                            showApp(
                                this@MainActivity,
                                backUpModel.newAppPackage!!
                            )
                        }
                        .setNegativeButton(
                            android.R.string.no
                        ) { dialog, which -> dialog.cancel() }
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show()
                }
            }
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        pres = getSharedPreferences("setting", 0) //load the preferences
        lockScreenShow = pres.getBoolean("lock", false)
        answer = pres.getString("answer", "")
        passWord = pres.getString("passWord", "")
        possQues = pres.getInt("possQues", 0)
        video = pres.getBoolean("video", true)
        val packageName = this.packageName
        if (answer == "") {
            dialogSupport()
        }
        loadBannerFB()
        loadInterFB()
        permission = findViewById<ImageView>(R.id.permission)
        permission.setOnClickListener(View.OnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        })
        wallpaper = findViewById<ImageView>(R.id.wallpaper)
        wallpaper.setOnClickListener(View.OnClickListener {
            val myIntent = Intent(applicationContext, ListWallpaperActivity::class.java)
            startActivity(myIntent)
        })
        disable_lock = findViewById<ImageView>(R.id.disable_lock)
        disable_lock.setOnClickListener(View.OnClickListener {
            val intent = Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD)
            startActivity(intent)
        })
        setVideo = findViewById<LinearLayout>(R.id.setVideo)
        btn_video = findViewById<ImageView>(R.id.btn_video)
        if (video) {
            btn_video.setImageResource(R.mipmap.btn_on)
        } else {
            btn_video.setImageResource(R.mipmap.btn_off)
        }
        setVideo.setOnClickListener(View.OnClickListener {
            showInterFb(this@MainActivity)
            if (video) {
                btn_video.setImageResource(R.mipmap.btn_off)
                video = false
                val settings = getSharedPreferences("setting", 0)
                val edit = settings.edit()
                edit.putBoolean("video", false)
                edit.commit()
            } else {
                btn_video.setImageResource(R.mipmap.btn_on)
                video = true
                val settings = getSharedPreferences("setting", 0)
                val edit = settings.edit()
                edit.putBoolean("video", true)
                edit.commit()
            }
        })
        setLockScreen = findViewById<LinearLayout>(R.id.setLockScreen)
        btn_lockScreen = findViewById<ImageView>(R.id.btn_lockScreen)
        LockScreen.getInstance(this)!!.init(this, true)
        if (LockScreen.getInstance(this)!!.isActive) {
            btn_lockScreen.setImageResource(R.mipmap.btn_on)
        } else {
            btn_lockScreen.setImageResource(R.mipmap.btn_off)
        }
        setLockScreen.setOnClickListener(View.OnClickListener { view ->
            if (answer == "") {
                dialogQuestion()
            } else {
                if (LockScreen.getInstance(this)!!.isActive) {
                    btn_lockScreen.setImageResource(R.mipmap.btn_off)
                    LockScreen.getInstance(this)!!.deactivate()
                    val settings = getSharedPreferences("setting", 0)
                    val edit = settings.edit()
                    edit.putBoolean("lock", false)
                    edit.commit()
                    showInterFb(this@MainActivity)
                } else {
                    if (passWord == "") {
                        val myIntent = Intent(view.context, ChangPassWordActivity::class.java)
                        startActivity(myIntent)
                    } else {
                        LockScreen.getInstance(this)!!.active()
                        val settings = getSharedPreferences("setting", 0)
                        val edit = settings.edit()
                        edit.putBoolean("lock", true)
                        edit.commit()
                        btn_lockScreen.setImageResource(R.mipmap.btn_on)
                        showInterFb(this@MainActivity)
                    }
                }
            }
        })
        preview = findViewById<ImageView>(R.id.preview)
        preview.setOnClickListener(View.OnClickListener { view -> //                showInterFb(MainActivity.this);
            if (passWord == "") {
                val myIntent = Intent(applicationContext, LockScreenActivity::class.java)
                myIntent.putExtra("test_lock", true)
                startActivity(myIntent)
            } else {
                val myIntent = Intent(applicationContext, LockScreenActivity::class.java)
                myIntent.putExtra("test_lock", true)
                startActivity(myIntent)
            }
        })
        changeQuestion = findViewById<ImageView>(R.id.changeQuestion)
        changeQuestion.setOnClickListener(View.OnClickListener { dialogQuestion() })
        changePassWord = findViewById<ImageView>(R.id.changPassWord)
        changePassWord.setOnClickListener(View.OnClickListener { view ->
            showInterFb(this@MainActivity)
            val myIntent = Intent(view.context, ChangPassWordActivity::class.java)
            startActivity(myIntent)
        })


//        SharedPreferences prefs = getSharedPreferences("rate_dialog", MODE_PRIVATE);
//        Boolean rated = prefs.getBoolean("rate", false);
//        if (!rated) {
//            showRateDialog();
//        }
        support = findViewById<ImageView>(R.id.support)
        support.setOnClickListener(View.OnClickListener { dialogSupport() })
    }

    override fun onResume() {
        if (LockScreen.getInstance(this)!!.isActive) {
            btn_lockScreen!!.setImageResource(R.mipmap.btn_on)
        } else {
            btn_lockScreen!!.setImageResource(R.mipmap.btn_off)
        }
        super.onResume()
    }

    private fun dialogQuestion() {
        Log.d("Answer", "dialogLogin: " + answer)
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom)
        dialog.show()
        val dropdown = dialog.findViewById<Spinner>(R.id.spinnerQuestion)
        val items = listQuestion
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter
        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                // your code here
                possQues2 = position
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
        val answerEdit = dialog.findViewById<EditText>(R.id.answer)
        answerEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

                // you can call or do what you want with your EditText here
                answer2 = s.toString()
                // yourEditText...
                Log.d("TAG", "afterTextChanged: $s")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        val dialogWindowWidth = (displayWidth * 0.9).toInt()
        val dialogWindowHeight = (displayHeight * 0.7).toInt()
        layoutParams.width = dialogWindowWidth
        layoutParams.height = dialogWindowHeight
        dialog.window!!.attributes = layoutParams
        val save = dialog.findViewById<ImageView>(R.id.saveAnswer)
        save.setOnClickListener {
            if (answer2.length >= 3) {
                saveAnswer(
                    applicationContext,
                    answer2,
                    possQues2
                )
                answer = answer2
                possQues = possQues2
                Toast.makeText(
                    this@MainActivity, "Question and Answer has been saved!",
                    Toast.LENGTH_LONG
                ).show()
                dialog.cancel()
            } else {
                Toast.makeText(
                    this@MainActivity, "You should chose Question or Answer more than 3 character!",
                    Toast.LENGTH_LONG
                ).show()
            }
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

    private inner class GetBackUp : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            val sh = HttpHandler()
            // Making a request to url and getting response
            val url =
                "https://raw.githubusercontent.com/gquan1402/amonglockscreen/main/backupdata.json"
            val jsonStr: String = sh.makeServiceCall(url)!!
            if (jsonStr != null) {
                try {
                    val jsonObj = JSONObject(jsonStr)
                    val appPackage = jsonObj.getString("appPackage")
                    val isLive = jsonObj.getBoolean("isLive")
                    val newAppPackage = jsonObj.getString("newAppPackage")
                    val isAdsShow = jsonObj.getBoolean("isAdsShow")
                    val inter = jsonObj.getString("inter")
                    val fb_inter = jsonObj.getString("fb_inter")
                    val fb_native = jsonObj.getString("fb_native")
                    val fb_banner = jsonObj.getString("fb_banner")
                    val isShowGG = jsonObj.getBoolean("isShowGG")
                    val banner = jsonObj.getString("banner")
                    val nativeAd = jsonObj.getString("nativeAd")
                    val rewarded = jsonObj.getString("rewarded")
                    val percent_banner = jsonObj.getInt("percent_banner")
                    val percent_inter = jsonObj.getInt("percent_inter")
                    val percent_native = jsonObj.getInt("percent_native")
                    val numberNativeAd = jsonObj.getInt("numberNativeAd")
                    val numberInterAd = jsonObj.getInt("numberInterAd")
                    val timeRequestAd = jsonObj.getInt("timeRequestAd")
                    backUpModel = BackUpModel()
                    backUpModel.appPackage = appPackage
                    backUpModel.isLive = isLive
                    backUpModel.newAppPackage = newAppPackage
                    backUpModel.isAdsShow = isAdsShow
                    backUpModel.inter = inter
                    backUpModel.fb_inter = fb_inter
                    backUpModel.fb_native = fb_native
                    backUpModel.fb_banner = fb_banner
                    backUpModel.isShowGG = isShowGG
                    backUpModel.banner = banner
                    backUpModel.nativeAd = nativeAd
                    backUpModel.rewarded = rewarded
                    backUpModel.percent_banner = percent_banner
                    backUpModel.percent_inter = percent_inter
                    backUpModel.percent_native = percent_native
                    backUpModel.numberNativeAd = numberNativeAd
                    backUpModel.numberInterAd = numberInterAd
                    backUpModel.timeRequestAd = timeRequestAd
                    INTER_ID = backUpModel.inter.toString()
                    BANNER_ID = backUpModel.banner.toString()
                    NATIVE_AD_ID = backUpModel.nativeAd.toString()
                    INTER_ID_FB = backUpModel.fb_inter.toString()
                    BANNER_ID_FB = backUpModel.fb_banner.toString()
                    NATIVE_ID_FB = backUpModel.fb_native.toString()
                    PERCENT_SHOW_BANNER_AD = backUpModel.percent_banner
                    PERCENT_SHOW_INTER_AD = backUpModel.percent_inter
                    PERCENT_SHOW_NATIVE_AD = backUpModel.percent_native
                    NUMBER_OF_NATIVE_AD = backUpModel.numberNativeAd
                    NUMBER_OF_INTER = backUpModel.numberInterAd
                    TIME_REQUEST_ADS = backUpModel.timeRequestAd
                } catch (e: JSONException) {
                    runOnUiThread { }
                }
            } else {
                runOnUiThread { }
            }
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainActivity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    override fun onDestroy() {
        if (adView != null) {
            adView!!.destroy()
        }
        if (interstitialAd != null) {
            interstitialAd!!.destroy()
        }
        super.onDestroy()
    }

    fun loadBannerFB() {
        val adContainer = findViewById<View>(R.id.adMobView)
        AudienceNetworkAds.initialize(this)
        adView = AdView(this, BANNER_ID_FB, AdSize.BANNER_HEIGHT_50)
        (adContainer as LinearLayout).addView(adView)

// Request an ad
        val adListener: AdListener = object : AdListener {
            override fun onError(ad: Ad, adError: AdError) {
                //loadBannerGG();
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
        if (ads >= PERCENT_SHOW_BANNER_AD) {
            adView!!.destroy()
            adView!!.visibility = View.GONE
        }
    }

    fun loadInterFB() {
        interstitialAd = InterstitialAd(this@MainActivity, INTER_ID_FB)
        // Create listeners for the Interstitial Ad
        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback
                Log.e("TAG", "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                Log.e("TAG", "Interstitial ad dismissed.")
                Handler().postDelayed({ loadInterFB() }, TIME_REQUEST_ADS.toLong())
            }

            override fun onError(ad: Ad, adError: AdError) {
                // Ad error callback
                Log.e("TAG", "Interstitial ad failed to load: " + adError.errorMessage)
                //loadInterGG();
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("TAG", "Interstitial ad is loaded and ready to be displayed!")
                // Show the ad
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
                Log.d("TAG", "Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                Log.d("TAG", "Interstitial ad impression logged!")
            }
        }

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd!!.loadAd(
            interstitialAd!!.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        )
    }

    companion object {
        var height = 0
        var width = 0
        var notificationId = 1090
        lateinit var pres: SharedPreferences
        var lockScreenShow: Boolean? = null
        var vibrate: Boolean? = null
        var listQuestion = arrayOf(
            "What is your first pet's name?",
            "What is your favorite color?",
            "What is your favorite food"
        )
        var answer: String? = null
        var possQues = 0
        var answer2 = ""
        var passWord: String? = ""
        var possQues2 = 0
        var video = false
        var INTER_ID_FB = "1"
        var BANNER_ID_FB = "1"
        var NATIVE_ID_FB = "1"
        var PERCENT_SHOW_BANNER_AD = 100
        var PERCENT_SHOW_INTER_AD = 100
        var PERCENT_SHOW_NATIVE_AD = 100
        var NUMBER_OF_NATIVE_AD = 3
        var TIME_REQUEST_ADS = 5000
        var NUMBER_OF_INTER = 15
        var NATIVE_AD_ID = "ca-app-pub-2991887587140391/9516628296"
        var INTER_ID = "ca-app-pub-2991887587140391/5720853518"
        var BANNER_ID = "ca-app-pub-2991887587140391/1829709967"
        var interstitialAd: InterstitialAd? = null
        fun showApp(context: Context, pkg: String) {
            val intent = Intent(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$pkg")
                )
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun saveAnswer(context: Context, text: String?, question: Int) {
            val sharedPreferences = context.getSharedPreferences("setting", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("answer", text)
            editor.putInt("possQues", question)
            editor.apply()
        }

        fun rateApp(context: Context) {
            val intent = Intent(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
                )
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun showInterFb(context: Context?) {
            if (interstitialAd == null || !interstitialAd!!.isAdLoaded) {

                //showInterstitial(context);
            } else {
                // Show the ad
                interstitialAd!!.show()
            }
        }
    }
}
