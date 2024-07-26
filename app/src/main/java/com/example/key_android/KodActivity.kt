package com.example.key_android

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import android.os.CountDownTimer
import android.widget.ProgressBar
import android.widget.Toast
import android.util.Log

class KodActivity : AppCompatActivity() {

    private lateinit var textKod: TextView
    private lateinit var progressBar: ProgressBar
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kod)

        val login = intent.getStringExtra("login") ?: ""

        if (login.isNotEmpty()) {
             sendLoginToServer(login)
         }
        progressBar = findViewById(R.id.kod_progressbar)
        textKod = findViewById(R.id.kod_kod)
        val linkToBack: TextView = findViewById(R.id.kod_link)
        linkToBack.setOnClickListener {
            val intentToBack = Intent(this, StartActivity::class.java)
            startActivity(intentToBack)
        }
        fetchDataFromServer()
        startCountdown()
    }



    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = ((millisUntilFinished / 1000) * 100 / 30).toInt()
                progressBar.progress = progress
            }
            override fun onFinish() {
                fetchDataFromServer()
                startCountdown()
            }
        }
        countDownTimer?.start()
    }

    private fun fetchDataFromServer() {
        GetKey.apice.fetchData().enqueue(object : Callback<Kod> {
            override fun onResponse(call: Call<Kod>, response: Response<Kod>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    val result = data?.code
                    textKod.text = result.toString()
                } else {
                    Toast.makeText(this@KodActivity, "Кода нет.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Kod>, t: Throwable) {
                Toast.makeText(this@KodActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun sendLoginToServer(login: String) {
         val loginData = LoginData(login)
         GetKey.apice.sendLogin(loginData).enqueue(object : Callback<Void> {
             override fun onResponse(call: Call<Void>, response: Response<Void>) {
                 if (response.isSuccessful) {
                     Toast.makeText(this@KodActivity, "Логин передан", Toast.LENGTH_SHORT).show()
                 } else {
                     Toast.makeText(this@KodActivity, "Ошибка отправки логина", Toast.LENGTH_SHORT).show()
                 }
             }
             override fun onFailure(call: Call<Void>, t: Throwable) {
                 Toast.makeText(this@KodActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
             }
         })
     }
    override fun onPause() {
        super.onPause()
        countDownTimer?.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (countDownTimer == null) {
            startCountdown()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }


}