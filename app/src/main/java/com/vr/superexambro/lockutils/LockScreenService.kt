package com.vr.superexambro.lockutils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import com.vr.superexambro.R
import com.vr.superexambro.lockactivity.LockScreenActivity
import com.vr.superexambro.lockactivity.MainActivity

class LockScreenService : Service() {
    private var mContext: Context? = null
    private var mNM: NotificationManager? = null
    private val mLockScreenReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (null != context && null != intent.action) {
                if (intent.action == Intent.ACTION_SCREEN_ON) {
                    startLockScreenActivity()
                }
            }
        }
    }

    private fun stateReceiver(isStartReceiver: Boolean) {
        if (isStartReceiver) {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_ON)
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            registerReceiver(mLockScreenReceiver, filter)
        } else {
            mLockScreenReceiver?.let { unregisterReceiver(it) }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        mNM = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startMyOwnForeground() else startForeground(
            1,
            Notification()
        )
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        stateReceiver(true)
        //        showNotification();
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        stateReceiver(false)
        mNM!!.cancel(MainActivity.notificationId)
        //        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        broadcastIntent.setClass(this, Restarter.class);
//        this.sendBroadcast(broadcastIntent);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            stopForeground(true);
//        } else {
//            stopSelf();
//        }
    }

    private fun startMyOwnForeground() {

//        String CHANNEL_ID = "lock_channel_01";
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        assert manager != null;
//// only use NotificationChannel when Api Level >= 26
//        if(Build.VERSION.SDK_INT >= 26) {
//            CharSequence name = "Among Us LockScreen";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.enableVibration(true);
//            mChannel.setShowBadge(false);
//            manager.createNotificationChannel(mChannel);
//
//        }
//
////Send push notification
//        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                .setContentTitle("Among Us LockScreen")
//                .setContentText("App is running in background.")
//                .setSmallIcon(R.mipmap.ic_launcher_old.png)
//                .setPriority(NotificationManager.IMPORTANCE_MIN)
//                .setCategory(Notification.CATEGORY_SERVICE)
//                .build();
//
//
//        startForeground(2, notification);
        val notificationIntent = Intent(this, LockScreenService::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)
        val notification: Notification = Notification.Builder(this)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("App is running in background.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            AppUtils.startMyOwnForeground(
                this,
                LockScreenService::class.java.simpleName, NOTIFICATION_ID
            )
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun startLockScreenActivity() {
        val startLockScreenActIntent = Intent(mContext, LockScreenActivity::class.java)
        startLockScreenActIntent.action = Intent.ACTION_VIEW
        startLockScreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startLockScreenActIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(startLockScreenActIntent)
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
    }

    private fun showNotification() {
//        CharSequence text = "Running";
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);
//
//        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.cerberus)
//                .setTicker(text)
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle(getText(R.string.app_name))
//                .setContentText(text)
//                .setContentIntent(contentIntent)
//                .setOngoing(true)
//                .build();
//
////        startForeground(101, notification);
//        mNM.notify(MainActivity.notificationId, notification);
    }

    companion object {
        private const val NOTIFICATION_ID = 23
    }
}