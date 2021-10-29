package com.example.cafe.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.cafe.R
import com.example.cafe.preference.PrefManager
import com.example.cafe.ui.home.MainActivity
import com.example.cafe.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    private val pref by lazy { PrefManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            if (pref.getBoolean("pref_user_login")!!) {
                when (pref.getString("pref_user_level")!!) {
                    "kasir" -> {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    "owner" -> {
//                        startActivity(Intent(this, TransactionActivity::class.java))
                    }
                    "chef" -> {

                    }
                }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000)
    }
}