package com.example.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat
import com.example.notification.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var notificationManager : NotificationManager? = null
    private var CHANNEL_ID = "channel_id"
    private var ACTION_SNOOZE = ""

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Register Channel kedalam sistem
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(CHANNEL_ID, "Countdown", "ini merupakan description")


        //tombol
        binding.btnStart.setOnClickListener {
            countDownTimer.start()
        }
        //timer
        countDownTimer = object : CountDownTimer(5000, 1000){
            override fun onTick(p0: Long) {
                binding.timer.text = getString(R.string.time_reamining, p0/1000)
            }

            override fun onFinish() {
                displayNotification()
            }

        }

    }

    private fun displayNotification(){
        //try yourself code with

        // untuk jika di click bisa ke acticty atau fragment yang di tuju
        val intent = Intent(this, SecondActivity::class.java)
//         tidak wajib
//            .apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val notificationID = 101

        val snoozeIntent = Intent(this, SecondActivity::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }

        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)

        //untuk apa saja yang ditampilkan di notifikasi
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Bandung")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Halo-halo Bandung\n" +
                        "Ibu kota Periangan\n" +
                        "Halo-halo Bandung\n" +
                        "Kota kenang-kenangan\n" +
                        "Sudah lama beta\n" +
                        "Tidak berjumpa dengan kau\n" +
                        "Sekarang telah menjadi lautan api\n" +
                        "Mari bung rebut kembali"))
            .setContentIntent(pendingIntent)
                //buat isi tombol
            .addAction(R.drawable.ic_baseline_snooze_24, getString(R.string.snooze),
                snoozePendingIntent)
            .build()

        // menampilkan notifikasi
        notificationManager?.notify(notificationID, builder)
    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String){
        //validasi notif akan dibuat juga version SDK 26+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}