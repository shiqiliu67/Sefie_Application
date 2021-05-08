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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shiqiliu.selfieapp.R
import com.shiqiliu.selfieapp.models.User
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    var uid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        uid = auth.uid
        databaseReference = firebaseDatabase.getReference("/users/$uid")

        init()
    }

    private fun init() {
       button_login.setOnClickListener {
           var email = edit_text_login_email.text.toString()
           var password = edit_text_login_password.text.toString()
             auth.signInWithEmailAndPassword(email,password)
               .addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
                   override fun onComplete(p0: Task<AuthResult>) {
                       if(p0.isSuccessful){
                           Toast.makeText(applicationContext,"Login Successful",Toast.LENGTH_SHORT).show()
                           Log.d("abc","login Successful")
//                           //insert user data
//                           var user = User(uid,email,null,null)
//                           databaseReference.setValue(user)
//                               .addOnSuccessListener {
//                                   Log.d("abc", "Save to the database")
//                                   Toast.makeText(applicationContext, "Successful save", Toast.LENGTH_SHORT).show()
//                               }
                           //intent to main activity
                         startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                       }
                       else{
                           Toast.makeText(applicationContext,"Login Failed", Toast.LENGTH_SHORT).show()
                       }
                   }
               })



       }

        text_view_login_clicktext_jump.setOnClickListener {
            startActivity(Intent(this,RegsiterActivity::class.java))
        }
    }
}