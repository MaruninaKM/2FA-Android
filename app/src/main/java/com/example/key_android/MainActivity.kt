package com.example.key_android
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)

        val AuthUserLog: EditText = findViewById(R.id.auth_log)
        val AuthPass: EditText = findViewById(R.id.auth_pas)
        val AuthButt: Button = findViewById(R.id.auth_button)
        val linkToStart: TextView = findViewById(R.id.auth_link)
        linkToStart.setOnClickListener {
            val intentToStart = Intent(this, StartActivity::class.java)
            startActivity(intentToStart)
        }
        AuthButt.setOnClickListener {
            val login = AuthUserLog.text.toString().trim()
            val pass = AuthPass.text.toString().trim()
            if (login == "" || pass == "") {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            }
            else {
                val data = UserData(login, pass)
                ConectServ.instance.userLogin(data).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@MainActivity, "Успешный вход!", Toast.LENGTH_SHORT).show()
                         //   val intentKod = Intent(this@MainActivity, KodActivity::class.java)
                          //  startActivity(intentKod)
                            val intentKod = Intent(this@MainActivity, KodActivity::class.java)
                            intentKod.putExtra("login", login)
                            startActivity(intentKod)
                        }
                        else {
                            Toast.makeText(this@MainActivity, "Неверные данные", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}