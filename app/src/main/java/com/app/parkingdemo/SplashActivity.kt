package com.app.parkingdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.rbddevs.splashy.Splashy

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setSplashy()
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
    }
    private fun setSplashy() {
        Splashy(this)
            .setLogo(R.mipmap.ic_launcher)
            .setAnimation(Splashy.Animation.GROW_LOGO_FROM_CENTER)
            .setBackgroundResource(R.color.black)
            .setTitleColor(R.color.white)
            .setProgressColor(R.color.white)
            .setTitle(R.string.app_name)
            //.setSubTitle(R.string.splash_screen_made_easy)
            .setFullScreen(false)
            .setClickToHide(true)
            .setDuration(2000)
            .show()

        Splashy.onComplete(object : Splashy.OnComplete {
            override fun onComplete() {
                val intent = Intent(this@SplashActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}