package com.vr.superexambro.lockactivity

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vr.superexambro.R

class ChangPassWordActivity : AppCompatActivity() {
    private var answer = ""
    private var actualAnswer = ""
    private var player = MediaPlayer()
    private lateinit var btn0 : Button
    private lateinit var btn1 : Button
    private lateinit var btn2 : Button
    private lateinit var btn3 : Button
    private lateinit var btn4 : Button
    private lateinit var btn5 : Button
    private lateinit var btn6 : Button
    private lateinit var btn7 : Button
    private lateinit var btn8 : Button
    private lateinit var btn9 : Button
    private lateinit var btnCancel : Button
    private lateinit var btnDone : Button

    private lateinit var passWordNum: TextView
    private lateinit var captionTextView: TextView
    private var newPass = ""
    private val verifyPass = ""
    private lateinit var cancel: ImageView

    private var step = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chang_pass_word)
        captionTextView = findViewById<TextView>(R.id.caption)
        passWordNum = findViewById<TextView>(R.id.passWordNum)
        btn0 = findViewById<Button>(R.id.unlock_button_0)
        btn1 = findViewById<Button>(R.id.unlock_button_1)
        btn2 = findViewById<Button>(R.id.unlock_button_2)
        btn3 = findViewById<Button>(R.id.unlock_button_3)
        btn4 = findViewById<Button>(R.id.unlock_button_4)
        btn5 = findViewById<Button>(R.id.unlock_button_5)
        btn6 = findViewById<Button>(R.id.unlock_button_6)
        btn7 = findViewById<Button>(R.id.unlock_button_7)
        btn8 = findViewById<Button>(R.id.unlock_button_8)
        btn9 = findViewById<Button>(R.id.unlock_button_9)
        btnCancel = findViewById<Button>(R.id.unlock_button_cancel)
        btnDone = findViewById<Button>(R.id.unlock_button_done)
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
        btn7.setOnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "7"
            }
            setTextPassWord()
        }
        btn8.setOnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "8"
            }
            setTextPassWord()
        }
        btn9.setOnClickListener {
            startSound(R.raw.beep_key)
            if (actualAnswer.length <= 4) {
                actualAnswer += "*"
                answer += "9"
            }
            setTextPassWord()
        }
        btnDone.setOnClickListener{
            if (step == 0) {
                if (answer == MainActivity.passWord) {
                    step = 1
                    answer = ""
                    actualAnswer = ""
                    setTextPassWord()
                    startSound(R.raw.done)
                    captionTextView.setText("New Password")
                } else {
                    startSound(R.raw.death)
                    val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(100)
                    Toast.makeText(
                        this@ChangPassWordActivity, "Password wrong!",
                        Toast.LENGTH_LONG
                    ).show()
                    answer = ""
                    actualAnswer = ""
                    setTextPassWord()
                }
            } else if (step == 1) {
                if (actualAnswer.length < 4) {
                    Toast.makeText(
                        this@ChangPassWordActivity, "Password must be at least 4 digits!",
                        Toast.LENGTH_LONG
                    ).show()
                    startSound(R.raw.death)
                    val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(100)
                    actualAnswer = ""
                    answer = ""
                    setTextPassWord()
                } else {
                    newPass = answer
                    step = 2
                    captionTextView.setText("Verify Password")
                    actualAnswer = ""
                    answer = ""
                    startSound(R.raw.done)
                    setTextPassWord()
                }
            } else {
                if (answer == newPass) {
                    startSound(R.raw.done)
                    val sharedPreferences: SharedPreferences =
                        this@ChangPassWordActivity.getSharedPreferences("setting", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("passWord", answer)
                    editor.apply()
                    MainActivity.passWord = answer
                    Toast.makeText(
                        this@ChangPassWordActivity, "Password has been saved!",
                        Toast.LENGTH_LONG
                    ).show()
                    MainActivity.showInterFb(this@ChangPassWordActivity)
                    finish()
                } else {
                    startSound(R.raw.death)
                    val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(100)
                    actualAnswer = ""
                    answer = ""
                    setTextPassWord()
                    Toast.makeText(
                        this@ChangPassWordActivity, "Password incorrect!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        btnCancel.setOnClickListener(View.OnClickListener {
            startSound(R.raw.beep_key)
            answer = ""
            actualAnswer = ""
            setTextPassWord()
        })
        cancel = findViewById<ImageView>(R.id.cancel)
        cancel.setOnClickListener(View.OnClickListener { finish() })
        if (MainActivity.passWord.equals("")) {
            step = 1
            captionTextView.setText("New Password")
        } else {
            captionTextView.setText("Current Password")
            step = 0
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
}