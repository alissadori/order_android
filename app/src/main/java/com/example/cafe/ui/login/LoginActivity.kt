package com.example.cafe.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cafe.R
import com.example.cafe.preference.PrefManager
import com.example.cafe.retrofit.ApiService
import com.example.cafe.retrofit.response.login.Login
import com.example.cafe.retrofit.response.login.LoginResponse
import com.example.cafe.ui.home.MainActivity
import com.example.cafe.ui.transaction.TransactionActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val api by lazy { ApiService.auth }
    private val pref by lazy { PrefManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        loadingLogin(false)
    }

    private fun setupListener() {
        btn_login.setOnClickListener {
            if (et_username.text.isNullOrEmpty() || et_password.text.isNullOrEmpty()) {
                Toast.makeText(applicationContext, "Isi Data Dengan Benar", Toast.LENGTH_SHORT)
                    .show()
            } else {
                login(et_username.text.toString(), et_password.text.toString())
            }
        }
    }

    private fun login(username: String, password: String) {
        loadingLogin(true)
        api.login(username, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    loadingLogin(false)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            loginResponse(it)
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loadingLogin(false)
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadingLogin(loading: Boolean) {
        when (loading) {
            true -> {
                pb_login.visibility = View.VISIBLE
                btn_login.visibility = View.GONE
            }
            false -> {
                pb_login.visibility = View.GONE
                btn_login.visibility = View.VISIBLE
            }
        }
    }

    private fun loginResponse(loginResponse: LoginResponse) {
        when (loginResponse.error) {
            true -> {
                Toast.makeText(
                    applicationContext, loginResponse.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            false -> {
                Toast.makeText(
                    applicationContext, loginResponse.message,
                    Toast.LENGTH_SHORT
                ).show()
                saveUser(loginResponse.data)
                when (loginResponse.data.level) {
                    "kasir" -> {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    "owner" -> {
                        startActivity(Intent(this, TransactionActivity::class.java))
                    }
                    "chef" -> {

                    }
                }
                finish()
            }
        }
    }

    private fun saveUser(data: Login) {
        pref.put("pref_user_login", true)
        pref.put("pref_user_name", data.nama)
        pref.put("pref_user_username", data.username)
        pref.put("pref_user_level", data.level)
    }

    private fun message(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}