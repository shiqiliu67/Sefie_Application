package com.shiqiliu.selfieapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.shiqiliu.selfieapp.R
import kotlinx.android.synthetic.main.activity_regsiter.*

class RegsiterActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regsiter)
        auth = FirebaseAuth.getInstance()
        init()
    }

    private fun init() {
        button_register.setOnClickListener {
            var email = edit_text_register_email.text.toString()
            var password = edit_text_register_password.text.toString()
            var name = edit_text_register_name.text.toString()
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,object : OnCompleteListener<AuthResult>{
                    override fun onComplete(p0: Task<AuthResult>) {
                        if(p0.isSuccessful){
                            Toast.makeText(applicationContext,"Successful", Toast.LENGTH_SHORT).show()
                            Log.d("abc","successful")
                            startActivity(Intent(this@RegsiterActivity,LoginActivity::class.java))
                        }
                        else{
                            Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }

        text_view_register_clicktext_jump.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}