package com.shiqiliu.selfieapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shiqiliu.selfieapp.R
import com.shiqiliu.selfieapp.adapter.PhotoAdapter
import com.shiqiliu.selfieapp.models.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var databaseReference: DatabaseReference
    lateinit var photoAdapter: PhotoAdapter
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference(User.COLL_NAME)
        init()
    }

    private fun init() {
        image_view_add_photo.setOnClickListener {
            startActivity(Intent(this,AddSelfieActivity::class.java))
        }

        photoAdapter = PhotoAdapter(this)
        recycler_view.adapter = photoAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    //put the user photo here
                    if(data.getValue(User::class.java)?.uid == auth.currentUser.uid){
                        var user = data.getValue(User::class.java)
                        Log.d("abc","user :$user")
                        Log.d("abc","userList:${user!!.imageUri}")
                        photoAdapter.setData(user)
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
}