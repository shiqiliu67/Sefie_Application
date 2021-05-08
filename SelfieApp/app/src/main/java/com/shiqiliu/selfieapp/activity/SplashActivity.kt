package com.shiqiliu.selfieapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.shiqiliu.selfieapp.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var h = Handler()
        var th = Thread(){
            kotlin.run {
                for(i in 5 downTo 1){
                    Thread.sleep(1000)
                    h.post {
                       // text_view_number.text = i.toString()
                        text_view_number.text = "Selfie App"
                    }
                }
                runOnUiThread{ Toast.makeText(this,"Welcome", Toast.LENGTH_LONG).show()}
            }
            var i = Intent(this,RegsiterActivity::class.java)
            startActivity(i)
        }
        th.start()
    }

}