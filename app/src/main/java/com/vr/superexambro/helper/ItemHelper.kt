package com.vr.superexambro.helper

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.vr.superexambro.R
import com.vr.superexambro.lockutils.LockScreen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun showSnackBar(view: View, message: String){
    //val rootView = findViewById<View>(android.R.id.content)
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}
fun generateRandomString(length: Int): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}
fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val currentDate = Date()
    return dateFormat.format(currentDate)
}
fun addMinutesToCurrentDate(minutesToAdd: Int): String {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.MINUTE, minutesToAdd)

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(calendar.time)
}
fun activateLockscreen(context:Context){
    LockScreen.getInstance(context)!!.active()
    val settings = context.getSharedPreferences("setting", 0)
    val edit = settings.edit()
    edit.putBoolean("lock", true)
    edit.commit()
}
fun deactivateLockScreen(context: Context){
    LockScreen.getInstance(context)!!.deactivate()
    val settings = context.getSharedPreferences("setting", 0)
    val edit = settings.edit()
    edit.putBoolean("lock", false)
    edit.commit()
}
fun formatDateToIndonesian(date: Date): String {
    val format = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    return format.format(date)
}
fun convertStringToDate(dateString: String, formatPattern: String): Date? {
    try {
        val dateFormat = SimpleDateFormat(formatPattern, Locale.getDefault())
        return dateFormat.parse(dateString)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}