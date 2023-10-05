package com.vr.superexambro.lockactivity

import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcel
import android.os.PowerManager.WakeLock
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.androidsx.rateme.OnRatingListener
import com.androidsx.rateme.OnRatingListener.RatingAction
import com.androidsx.rateme.RateMeDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.vr.superexambro.R
import com.vr.superexambro.helper.startTimer
import com.vr.superexambro.helper.unlockLockScreen
import com.vr.superexambro.helper.updateFirebase
import com.vr.superexambro.lockutils.LockScreen
import java.util.Timer
import java.util.TimerTask

class LockScreenActivity() : AppCompatActivity() {
    lateinit var btnRefresh :ImageButton
    lateinit var btnSelesai : LinearLayout
    lateinit var tvTimer  : TextView
    lateinit var contentView  : LinearLayout
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnYakin: Button
    private lateinit var btnKembali: Button
    private lateinit var lyKonfirm: RelativeLayout
    var durasi = ""
    var idUjian = ""
    private lateinit var pres: SharedPreferences
    private lateinit var handler: Handler
    lateinit var lockScreenTask: Runnable
    private var test_lock = false

    //BATAS OLD
    /*
    private val problemText = ""
    private var captionTextView: TextView? = null
    private var videoView: VideoView? = null
    private var answer = ""
    private var actualAnswer = ""
    private var player = MediaPlayer()
    private lateinit var  btn0 : Button
    private lateinit var  btn1 : Button
    private lateinit var  btn2 : Button
    private lateinit var  btn3 : Button
    private lateinit var  btn4 : Button
    private lateinit var  btn5 : Button
    private lateinit var  btn6 : Button
    private lateinit var  btn7 : Button
    private lateinit var  btn8 : Button
    private lateinit var  btn9 : Button
    private lateinit var  btnCancel : Button
    private lateinit var  btnDone : Button

    private lateinit var passWordNum: TextView
    private lateinit var handler: Handler
    private lateinit var view1: LinearLayout
    lateinit var death: RelativeLayout
    lateinit var lockScreenTask: Runnable
     lateinit var  forgotPass: ImageView
     var answerQues: String? = null
     lateinit var wakeLock: WakeLock
    private var test_lock = false
    private var password: String? = null
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_screen)

        initLock()
        initView()
        initClick()
        initWebview()
        initIntent()
        initTimer()
    }
    private fun initView(){
        btnRefresh = findViewById(R.id.btnRefresh)
        btnSelesai = findViewById(R.id.btnSelesai)
        tvTimer = findViewById(R.id.tvTimer)
        contentView = findViewById(R.id.contentView)
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        btnYakin = findViewById(R.id.btnYakin)
        btnKembali = findViewById(R.id.btnKembali)
        lyKonfirm = findViewById(R.id.lyKonfirm)
    }
    private fun initClick(){
        btnRefresh.setOnClickListener {
            btnRefresh.isEnabled = false
            progressBar.visibility = View.VISIBLE
            webView.reload()
        }
        btnSelesai.setOnClickListener {
            lyKonfirm.visibility = View.VISIBLE
        }
        btnKembali.setOnClickListener {
            lyKonfirm.visibility = View.GONE
        }
        btnYakin.setOnClickListener {
            updateFirebase("DetailActivity", FirebaseFirestore.getInstance(), "ujian",
                idUjian, hashMapOf("status" to "Selesai"))
            { unlockLockScreen(test_lock, this, this) }
        }
    }
    private fun initIntent(){
        durasi = intent.getStringExtra("durasi").toString()
        idUjian = intent.getStringExtra("documentId").toString()
    }
    private fun initTimer(){
        startTimer(contentView,durasi.toInt(), tvTimer, idUjian,this,test_lock,this)
    }
    fun initWebview() {
        /// Konfigurasi WebView
        webView.settings.javaScriptEnabled = true

        // Memuat URL awal
        loadWebPage("https://www.example.com")

        // Mengaktifkan WebViewClient
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                btnRefresh.isEnabled = true
            }
        }

        // Mengaktifkan WebChromeClient untuk loading bar
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }
    }
    private fun loadWebPage(url: String) {
        webView.loadUrl(url)
    }
    private fun initLock(){
        pres = getSharedPreferences("setting", 0)
        test_lock = intent.getBooleanExtra("test_lock", false)
        lockScreenTask = LockPhone(this)
        handler = Handler(Looper.getMainLooper())
    }
    private inner class LockPhone internal constructor(private val activity: LockScreenActivity) :
        Runnable {
        override fun run() {
//            DevicePolicyManager manager = ((DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE));
//            manager.lockNow();
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNavigationBar()
    }

    private fun hideNavigationBar() {
        val decorView = this.window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread { decorView.systemUiVisibility = uiOptions }
            }
        }
        timer.scheduleAtFixedRate(task, 1, 2)
    }

    override fun onAttachedToWindow() {
        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            )
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            //            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
//            keyguardManager.requestDismissKeyguard(this, null);
            val keyguardManager: KeyguardManager? =
                getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager?.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            )
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        super.onAttachedToWindow()
    }

    override fun onStart() {
        super.onStart()
        handler!!.postDelayed((lockScreenTask)!!, (10 * 1000).toLong())
    }

    override fun onStop() {
        super.onStop()
        handler!!.removeCallbacks((lockScreenTask)!!)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {}
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return false
    }

    // BATAS LAMA
    /*
    fun initOld(){
        pres = getSharedPreferences("setting", 0)
        password = pres.getString("passWord", "")
        test_lock = intent.getBooleanExtra("test_lock", false)
        lockScreenTask = LockPhone(this)
        captionTextView = findViewById(R.id.caption)
        handler = Handler(Looper.getMainLooper())
        death = findViewById<RelativeLayout>(R.id.death)
        view1 = findViewById(R.id.view1)
        passWordNum = findViewById(R.id.passWordNum)
        videoView = findViewById<VideoView>(R.id.videoView)
        btn0 = findViewById(R.id.unlock_button_0)
        btn1 = findViewById(R.id.unlock_button_1)
        btn2 = findViewById(R.id.unlock_button_2)
        btn3 = findViewById(R.id.unlock_button_3)
        btn4 = findViewById(R.id.unlock_button_4)
        btn5 = findViewById(R.id.unlock_button_5)
        btn6 = findViewById(R.id.unlock_button_6)
        btn7 = findViewById(R.id.unlock_button_7)
        btn8 = findViewById(R.id.unlock_button_8)
        btn9 = findViewById(R.id.unlock_button_9)
        btnCancel = findViewById(R.id.unlock_button_cancel)
        btnDone = findViewById(R.id.unlock_button_done)
        btn0.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "0"
            }
            setTextPassWord()
        })
        btn1.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "1"
            }
            setTextPassWord()
        })
        btn2.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "2"
            }
            setTextPassWord()
        })
        btn3.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "3"
            }
            setTextPassWord()
        })
        btn4.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "4"
            }
            setTextPassWord()
        })
        btn5.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "5"
            }
            setTextPassWord()
        })
        btn6.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "6"
            }
            setTextPassWord()
        })
        btn7.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "7"
            }
            setTextPassWord()
        })
        btn8.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "8"
            }
            setTextPassWord()
        })
        btn9.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "9"
            }
            setTextPassWord()
        })
        btnDone.setOnClickListener(View.OnClickListener {
            Log.d("TAG", "onClick: $answer")
            if ((answer == password)) {
                startSound(R.raw.done)
                if (test_lock) {
                    val prefs = getSharedPreferences("rate_dialog", MODE_PRIVATE)
                    val rated = prefs.getBoolean("rate", false)
                    if (!rated) {
                        showRateDialog()
                    } else {
                        //MainActivity.showInterstitial(LockScreenActivity.this);
                        finish()
                    }
                } else {
                    finish()
                }
            } else {
                startSound(R.raw.death)
                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(100)
                if (MainActivity.video) {
                    death()
                } else {
                    answer = ""
                    actualAnswer = ""
                    setTextPassWord()
                }
            }
        })
        btnCancel.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            answer = ""
            actualAnswer = ""
            setTextPassWord()
        })
        hideNavigationBar()
        forgotPass = findViewById<ImageView>(R.id.forgotPass)
        forgotPass.setOnClickListener(View.OnClickListener { dialogQuestion() })
    }
    private fun showRateDialog() {
        val rateMeDialog = RateMeDialog.Builder(packageName, getString(R.string.app_name))
            .setHeaderBackgroundColor(resources.getColor(R.color.dialog_primary))
            .setBodyBackgroundColor(resources.getColor(R.color.dialog_primary_light))
            .setBodyTextColor(resources.getColor(R.color.colorBlack))
            .setHeaderTextColor(resources.getColor(R.color.dialog_text_foreground))
            .showAppIcon(R.mipmap.ic_launcher)
            .setShowShareButton(true)
            .setRateButtonBackgroundColor(resources.getColor(R.color.dialog_primary))
            .setRateButtonPressedBackgroundColor(resources.getColor(R.color.dialog_primary_dark))
            .setOnRatingListener(object : OnRatingListener {
                override fun onRating(action: RatingAction, rating: Float) {
                    if (action == RatingAction.HIGH_RATING_WENT_TO_GOOGLE_PLAY || action == RatingAction.LOW_RATING_GAVE_FEEDBACK || action == RatingAction.LOW_RATING_REFUSED_TO_GIVE_FEEDBACK || action == RatingAction.LOW_RATING) {
                        rateApp(this@LockScreenActivity)
                        val editor = getSharedPreferences("rate_dialog", MODE_PRIVATE).edit()
                        editor.putBoolean("rate", true)
                        editor.commit()
                    }
                    finish()
                }

                override fun describeContents(): Int {
                    return 0
                }

                override fun writeToParcel(dest: Parcel, flags: Int) {
                    // Nothing to write
                }
            })
            .build()
        rateMeDialog.isCancelable = false
        rateMeDialog.show(fragmentManager, "custom-dialog")
    }

    private fun dialogQuestion() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom)
        dialog.show()
        val dropdown = dialog.findViewById<Spinner>(R.id.spinnerQuestion)
        val items = listQuestion
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter
        val answerEdit = dialog.findViewById<EditText>(R.id.answer)
        answerEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

                // you can call or do what you want with your EditText here
                answerQues = s.toString()
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
        val dialogWindowHeight = (displayHeight * 0.6).toInt()
        layoutParams.width = dialogWindowWidth
        layoutParams.height = dialogWindowHeight
        dialog.window!!.attributes = layoutParams
        val save = dialog.findViewById<ImageView>(R.id.saveAnswer)
        save.setOnClickListener { view ->
            if ((answerQues == MainActivity.answer)) {
                MainActivity.passWord = ""
                val sharedPreferences = getSharedPreferences(
                    "setting",
                    MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                editor.putString("passWord", "")
                editor.apply()
                LockScreen.getInstance(this)!!.deactivate()
                val myIntent = Intent(view.context, ChangPassWordActivity::class.java)
                startActivity(myIntent)
                finish()
            } else {
                Toast.makeText(
                    this@LockScreenActivity, "Answer Wrong!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun death() {
        view1!!.visibility = View.GONE
        death!!.visibility = View.VISIBLE
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        //specify the location of media file
        val path = "android.resource://" + packageName + "/" + R.raw.death1
        //Setting MediaController and URI, then starting the videoView
        videoView!!.setMediaController(null)
        videoView!!.setVideoURI(Uri.parse(path))
        videoView!!.requestFocus()
        videoView!!.start()
        videoView!!.setOnPreparedListener { mp -> mp.start() }
        videoView!!.setOnCompletionListener {
            view1!!.visibility = View.VISIBLE
            death!!.visibility = View.GONE
            answer = ""
            actualAnswer = ""
            setTextPassWord()
        }
    }

    private fun setTextPassWord() {
        passWordNum!!.text = actualAnswer
    }

    private fun startSound(resourceID: Int) {
        // player = new MediaPlayer();
        player = MediaPlayer.create(this, resourceID)
        player.start()
    }

    private inner class LockPhone internal constructor(private val activity: LockScreenActivity) :
        Runnable {
        override fun run() {
//            DevicePolicyManager manager = ((DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE));
//            manager.lockNow();
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNavigationBar()
    }

    private fun hideNavigationBar() {
        val decorView = this.window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread { decorView.systemUiVisibility = uiOptions }
            }
        }
        timer.scheduleAtFixedRate(task, 1, 2)
    }

    override fun onAttachedToWindow() {
        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            )
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            //            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
//            keyguardManager.requestDismissKeyguard(this, null);
            val keyguardManager: KeyguardManager? =
                getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager?.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            )
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        super.onAttachedToWindow()
    }

    override fun onStart() {
        super.onStart()
        handler!!.postDelayed((lockScreenTask)!!, (10 * 1000).toLong())
    }

    override fun onStop() {
        super.onStop()
        handler!!.removeCallbacks((lockScreenTask)!!)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {}
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return false
    }

    companion object {
        var listQuestion = arrayOf(
            "What is your first pet's name?",
            "What is your favorite color?",
            "What is your favorite food"
        )
        private lateinit var pres: SharedPreferences
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
    }

     */
}